package org.usfirst.frc4904.robot.input;


import java.util.ArrayList;
import java.util.Arrays;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * The LIDAR class lets other classes retrieve data from an Arduino
 * that reads from a physical Neato XV-11 LIDAR. It needs to be updated
 * to receive new data. It provides methods to read raw data, find
 * distances, and find lines (totes) via a Hough transform.
 */
public class LIDAR implements Updatable {
	private final LogKitten logger;
	private final SerialPort port; // Serial port used to talk to the LIDAR,
	private volatile int[] dists; // Array that contains distances to objects 360 degrees around the LIDAR.
	private volatile long[] distUpdateTime; // Array that represents the number of milliseconds since each distance was updated
	private static final int HOUGH_WIDTH = 1280 / 2; // The width of the Hough transform coordinate system
	private static final int HOUGH_HEIGHT = 720 / 2; // The height of the Hough transform coordinate system
	private static final double HOUGH_SCALING_FACTOR = 5; // Factor to scale coordinates by so they fit within the width and height.
	private static final int HOUGH_SENSITIVITY = 30;
	private static final int DATA_SAVE_TIME = 5000; // Amount of time to save LIDAR data (ms).
	public static final int LIDAR_MOUNT_OFFSET = -100; // mm to right. Cartesian. Because.
	public static final int GRABBER_LENGTH = 700; // Distance from LIDAR to grabber
	public static final int GRABBER_LENGTH_OFFSET = GRABBER_LENGTH + 100; // Go an extra 100 mm (to tell if lines are the grabber or totes)
	public static final int CORRECTED_ANGLE_BREADTH = 16; // How many angles to average when correcting an angle. Should be divisible by 4
	public static final boolean DISABLED = false;
	private static final double[] staticSinCache = new double[360];
	private static final double[] staticCosCache = new double[360];
	// Cache values
	static {
		for (int i = 0; i < 360; i++) {
			double radians = i * Math.PI / 180;
			staticCosCache[i] = Math.cos(radians + Math.PI / 2);
			staticSinCache[i] = Math.sin(radians + Math.PI / 2);
		}
	}
	
	public LIDAR() {
		dists = new int[360];
		logger = new LogKitten(LogKitten.LEVEL_WARN);
		try {
			port = new SerialPort(115200, SerialPort.Port.kMXP);
		}
		catch (Error e) {
			logger.f("Could not connect to LIDAR");
			throw e;
		}
	}
	
	/**
	 * Reads the distance from the LIDAR to wherever its beam hits at a certain angle relative to the chassis.
	 * 
	 * @param angle
	 *        The angle at which to return the distance, as an integer.
	 *        Note that 0 as the input means straight ahead - the function
	 *        shifts the data so 0 is to the right, like in normal polar coordinates.
	 * @return The requested distance as an integer.
	 */
	private int read(int angle) {
		// Make sure angle is within the correct range and shift it.
		// The angle is shifted by 90 so that 0 is to the right (like a normal graph.)
		// It is then modded. Lastly, we add 360 and mod again to account for negative angle inputs.
		angle = ((angle + 270 + 360) % 360);
		// Write to the port requesting distance at angle.
		port.flush(); // Flush port to make sure we get the data we ask for
		port.writeString(Integer.toString(angle) + "#");
		// Read response.
		while (port.getBytesReceived() < 2) {} // Wait till we receive the data
		String data = port.readString();
		if (data.indexOf('\n') <= 0) { // If there is no data or the only character is a newline
			logger.w("Got nonsensical data (no newline terminator) at angle " + angle);
			return 0;
		}
		data = data.substring(0, data.indexOf('\n') - 1);
		logger.d("Reading LIDAR at angle " + angle + " bytes received " + port.getBytesReceived() + " distance " + data);
		return Integer.parseInt(data); // Return data as integer
	}
	
	/**
	 * Returns the array of distances around the LIDAR. 90 is straight forward. Returns an updated array, so it returns quickly.
	 * 
	 * @return An int array of distances 360 degrees around the LIDAR.
	 */
	public int[] getDists() {
		return dists;
	}
	
	/**
	 * Uses a Hough transform to get the most prominent line the LIDAR can see. The Hough transform is done when this function is called, so it can be slow.
	 * 
	 * @return An int array of length 4, containing two ordered pairs with the XY coordinates of the line endpoints.
	 *         E.g. [3, 1, 7, 7] means a line with endpoints (3, 1) and (7, 7).
	 */
	public int[] getLine() {
		long time = System.currentTimeMillis();
		HoughTransform H = new HoughTransform(HOUGH_WIDTH, HOUGH_HEIGHT); // Make a new Hough transform canvas
		// For
		System.out.println("OCTOTHORPDANKSTART" + time);
		for (int i = 0; i < 180; i++) {
			if (dists[i] < 0 || dists[i] > 1500) {
				continue;
			}
			int[] xy = getXY(i);
			int x = xy[0];
			int y = xy[1];
			if (x != 320 && y != 180) {
				H.addPoint(x, y);
				System.out.println("#dank" + time + "," + x + "," + y);
			}
		}
		System.out.println("OCTOTHORPDANKEND" + time);
		System.out.println(System.currentTimeMillis() - time);
		ArrayList<int[]> inFront = new ArrayList<int[]>();
		for (HoughLine line : H.getLines(HOUGH_SENSITIVITY)) {
			logger.v("Checking a line");
			int[] tmpcoords = new int[4];
			tmpcoords = line.getCoordinates();
			if (tmpcoords[0] > 0 && tmpcoords[2] > 0) {
				// If the two Y coordinates are on opposites sides of the X axis (the line crosses the X axis)
				// and both X coordinates are positive,
				// assume that this is a line that we like, because it's directly in front of the sensor 0 degrees
				logger.v("Added line");
				inFront.add(tmpcoords);
			}
		}
		System.out.println(System.currentTimeMillis() - time);
		// TODO check the length of the lines to see which is the most reasonable
		// if (inFront.isEmpty()) logger.w("Line: " + Integer.toString(inFront.get(0)[0]) + " " + Integer.toString(inFront.get(0)[1]) + " " + Integer.toString(inFront.get(0)[2]) + " " + Integer.toString(inFront.get(0)[3]));
		return inFront.get(0);
	}
	
	/**
	 * Gets the distance at an angle
	 * 
	 * @param angle
	 * @return
	 */
	public int getCorrectedAngleDist(int angle) {
		int[] avDists = Arrays.copyOfRange(dists, angle - LIDAR.CORRECTED_ANGLE_BREADTH / 2, angle + LIDAR.CORRECTED_ANGLE_BREADTH / 2);
		Arrays.sort(avDists);
		int firstQuartile = avDists[LIDAR.CORRECTED_ANGLE_BREADTH * (1 / 4)];
		int median = avDists[LIDAR.CORRECTED_ANGLE_BREADTH * (2 / 4)];
		int thirdQuartile = avDists[LIDAR.CORRECTED_ANGLE_BREADTH * (3 / 4)];
		int trimean = (firstQuartile + median + thirdQuartile) / 3;
		return trimean;
	}
	
	public void update() {
		if (LIDAR.DISABLED) {
			return;
		}
		logger.d("Updating LIDAR");
		try {
			for (int i = 0; i < 360; i++) { // We only want the area in front of the LIDAR
				int data = read(i);
				if (data != 0) {
					dists[i] = data;
					distUpdateTime[i] = System.currentTimeMillis();
				} else if (dists[i] != 0 && distUpdateTime[i] < System.currentTimeMillis() + DATA_SAVE_TIME) {
					dists[i] = 0; // If the data is not updated for x seconds, replace it with 0 in order to prevent hough transform from getting weird.
				}
			}
		}
		catch (Exception e) {
			logger.e("Error updating LIDAR: ");
			e.printStackTrace();
		}
	}
	
	public int[] getXY(int angle) {
		int x = (int) (HOUGH_WIDTH / 2 + staticCosCache[angle] * dists[angle] / HOUGH_SCALING_FACTOR);
		int y = (int) (HOUGH_HEIGHT / 2 - staticSinCache[angle] * dists[angle] / HOUGH_SCALING_FACTOR);
		return new int[] {x, y};
	}
	
	public class HoughLine {
		double theta;
		double thetaSin;
		double thetaCos;
		double r;
		static final int greenSensitivityPixels = 5;
		
		public HoughLine(double theta, double r, double thetaSin, double thetaCos) {
			this.theta = theta;
			this.r = r;
			this.thetaSin = thetaSin;
			this.thetaCos = thetaCos;
		}
		
		/**
		 * Initializes the hough array. Called by the constructor so you don't need
		 * to call it yourself, however you can use it to reset the transform if you
		 * want to plug in another image (although that image must have the same
		 * width and height)
		 * 
		 * @return An int array. array[0],array[1] is first point of line, array[2],array[3] is the end of the line
		 */
		public int[] getCoordinates() {
			int[] X = new int[360];// X and Y coordinates of the 360 points
			int[] Y = new int[360];
			for (int i = 0; i < 360; i++) {
				int[] xy = getXY(i);
				X[i] = xy[0];
				Y[i] = xy[1];
			}
			// During processing h_h is doubled so that -ve r values
			int houghHeight = (int) (Math.sqrt(2) * Math.max(HOUGH_HEIGHT, HOUGH_WIDTH)) / 2;
			// Find edge points and vote in array
			float centerX = HOUGH_WIDTH / 2;
			float centerY = HOUGH_HEIGHT / 2;
			// Draw edges in output array
			double tsin = thetaSin;
			double tcos = thetaCos;
			ArrayList<ArrayList<int[]>> me = new ArrayList<ArrayList<int[]>>();// All the segments in this line
			boolean inLine = false;
			if (theta < Math.PI * 0.25 || theta > Math.PI * 0.75) {
				// Draw vertical-ish lines
				for (int y = 0; y < HOUGH_HEIGHT; y++) {
					int x = (int) ((r - houghHeight - (y - centerY) * tsin) / tcos + centerX);
					if (x < HOUGH_WIDTH && x >= 0) {
						inLine = process(x, y, X, Y, me, inLine);
					}
				}
			} else {
				// Draw horizontal-sh lines
				for (int x = 0; x < HOUGH_WIDTH; x++) {
					int y = (int) ((r - houghHeight - (x - centerX) * tcos) / tsin + centerY);
					if (y < HOUGH_HEIGHT && y >= 0) {
						inLine = process(x, y, X, Y, me, inLine);
					}
				}
			}
			int maxLen = 0;// Length of the longest segment
			int maxInd = -1;// Index of that segment
			for (int i = 0; i < me.size(); i++) {
				int size = me.get(i).size();
				if (size > maxLen) {
					maxLen = size;
					maxInd = i;
				}
			}
			ArrayList<int[]> a = me.get(maxInd);// a is the longest segment
			int[] A = a.get(0);
			int[] B = a.get(a.size() - 1);
			return new int[] {A[1], A[2], B[1], B[2]};
		}
		
		public boolean process(int x, int y, int[] X, int[] Y, ArrayList<ArrayList<int[]>> me, boolean inLine) {
			int max = greenSensitivityPixels * greenSensitivityPixels;// We only want points closer than the sensitivity
			int maxInd = -1;// Index of the closest point
			for (int i = 0; i < 360; i++) {
				int xx = X[i] - x;
				int yy = Y[i] - y;
				int dist = xx * xx + yy * yy;
				if (dist < max) {
					max = dist;
					maxInd = i;
				}
			}
			if (maxInd != -1) {// If there was a point closer than the sensitivity
				if (!inLine) {
					me.add(new ArrayList<int[]>());
					inLine = true;
				}
				me.get(me.size() - 1).add(new int[] {maxInd, x, y});
			} else {
				inLine = false;
			}
			return inLine;
		}
	}
	
	public class HoughTransform extends Thread {
		// The size of the neighbourhood in which to search for other local maxima
		final int neighbourhoodSize = 4;
		// How many discrete values of theta shall we check?
		final int maxTheta = 180;
		// Using maxTheta, work out the step
		final double thetaStep = Math.PI / maxTheta;
		// the width and height of the image
		protected int width, height;
		// the hough array
		protected int[][] houghArray;
		// the coordinates of the centre of the image
		protected float centerX, centerY;
		// the height of the hough array
		protected int houghHeight;
		// double the hough height (allows for negative numbers)
		protected int doubleHeight;
		// the number of points that have been added
		protected int numPoints;
		// cache of values of sin and cos for different theta values. Has a significant performance improvement.
		private double[] sinCache;
		private double[] cosCache;
		
		/**
		 * Initializes the hough transform. The dimensions of the input image are
		 * needed in order to initialize the hough array.
		 *
		 * @param width
		 *        The width of the input image
		 * @param height
		 *        The height of the input image
		 */
		public HoughTransform(int width, int height) {
			this.width = width;
			this.height = height;
			initialize();
		}
		
		/**
		 * Initializes the hough array. Called by the constructor so you don't need
		 * to call it yourself, however you can use it to reset the transform if you
		 * want to plug in another image (although that image must have the same
		 * width and height)
		 */
		private void initialize() {
			// Calculate the maximum height the hough array needs to have
			houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2;
			// Double the height of the hough array to cope with negative r values
			doubleHeight = 2 * houghHeight;
			// Create the hough array
			houghArray = new int[maxTheta][doubleHeight];
			// Find edge points and vote in array
			centerX = width / 2;
			centerY = height / 2;
			// Count how many points there are
			numPoints = 0;
			// Cache the values of sin and cos for faster processing
			sinCache = new double[maxTheta];
			cosCache = sinCache.clone();
			for (int t = 0; t < maxTheta; t++) {
				double realTheta = t * thetaStep;
				sinCache[t] = Math.sin(realTheta);
				cosCache[t] = Math.cos(realTheta);
			}
		}
		
		/**
		 * Adds a single point to the hough transform. You can use this method
		 * directly if your data isn't represented as a buffered image.
		 *
		 * @param x
		 * @param y
		 */
		public void addPoint(int x, int y) {
			// Go through each value of theta
			for (int t = 0; t < maxTheta; t++) {
				// Work out the r values for each theta step
				int r = (int) ((x - centerX) * cosCache[t] + (y - centerY) * sinCache[t]);
				// this copes with negative values of r
				r += houghHeight;
				if (r < 0 || r >= doubleHeight) {
					continue;
				}
				// Increment the hough array
				houghArray[t][r]++;
			}
			numPoints++;
		}
		
		/**
		 * Once points have been added in some way this method extracts the lines
		 * and returns them as a Vector of HoughLine objects, which can be used to
		 * draw on the
		 *
		 * @param threshold
		 * @return
		 */
		public ArrayList<HoughLine> getLines(int threshold) {
			// Initialize the vector of lines that we'll return
			ArrayList<HoughLine> lines = new ArrayList<HoughLine>(20);
			// Only proceed if the hough array is not empty
			if (numPoints == 0) {
				return lines;
			}
			// Search for local peaks above threshold to draw
			for (int t = 0; t < maxTheta; t++) {
				loop: for (int r = neighbourhoodSize; r < doubleHeight - neighbourhoodSize; r++) {
					// Only consider points above threshold
					if (houghArray[t][r] > threshold) {
						int peak = houghArray[t][r];
						// Check that this peak is indeed the local maxima
						for (int dx = -neighbourhoodSize; dx <= neighbourhoodSize; dx++) {
							for (int dy = -neighbourhoodSize; dy <= neighbourhoodSize; dy++) {
								int dt = t + dx;
								int dr = r + dy;
								if (dt < 0) {
									dt = dt + maxTheta;
								} else if (dt >= maxTheta) {
									dt = dt - maxTheta;
								}
								if (houghArray[dt][dr] > peak) {
									// found a bigger point nearby, skip
									continue loop;
								}
							}
						}
						// calculate the true value of theta
						double theta = t * thetaStep;
						double thetaSin = staticSinCache[t];
						double thetaCos = staticCosCache[t];
						// add the line to the vector
						lines.add(new HoughLine(theta, r, thetaSin, thetaCos));
					}
				}
			}
			return lines;
		}
		
		/**
		 * Gets the highest value in the hough array (the "strongest" line)
		 *
		 * @return
		 *
		 */
		public int getHighestValue() {
			int max = 0;
			for (int t = 0; t < maxTheta; t++) {
				for (int r = 0; r < doubleHeight; r++) {
					if (houghArray[t][r] > max) {
						max = houghArray[t][r];
					}
				}
			}
			return max;
		}
	}
}