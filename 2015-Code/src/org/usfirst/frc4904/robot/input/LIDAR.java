package org.usfirst.frc4904.robot.input;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class LIDAR implements Updatable {
	private volatile int[] dists;
	private final LogKitten logger;
	private final SerialPort port;
	private static final int width = 1280;// This is the resolution of my screen, because that seemed to work
	private static final int height = 720;
	private static final double d = 5;// Settings from my screen
	private static final int houghSensitivity = 30;
	public static final int LIDAR_MOUNT_OFFSET = -100; // mm to right. Cartesian. Because.
	public static final int GRABBER_LENGTH = 700; // Distance from LIDAR to grabber
	public static final int GRABBER_LENGTH_OFFSET = GRABBER_LENGTH + 100; // Go an extra 100 mm (to tell if lines are the grabber or totes)
	public static final int CORRECTED_ANGLE_BREADTH = 16; // How many angles to average when correcting an angle. Should be divisible by 4
	public static final boolean DISABLED = false;
	
	public LIDAR() {
		dists = new int[360];
		logger = new LogKitten(LogKitten.LEVEL_DEBUG);
		port = new SerialPort(115200, SerialPort.Port.kMXP);
	}
	
	private int read(int angle) {
		port.flush();
		System.out.print("Reading at angle " + angle + " value ");
		byte[] writeBuffer = BigInteger.valueOf(angle).toByteArray();
		port.write(writeBuffer, writeBuffer.length);
		while (port.getBytesReceived() == 0) {}
		String data = port.readString();
		System.out.println(writeBuffer.length + " " + port.getBytesReceived());
		System.out.println(data);
		return Integer.parseInt(data);
	}
	
	public int[] getDists() {
		return dists;
	}
	
	public int[] getLine() {
		HoughTransform H = new HoughTransform(width, height);
		for (int i = 0; i < 360; i++) {
			if (dists[i] < 0 || dists[i] > 1500) {
				dists[i] = 0;
			}
			int[] xy = getXY(i);
			int x = xy[0];
			int y = xy[1];
			H.addPoint(x, y);
			H.addPoint(x + 1, y);// We drew a 2x2 pixel square, wanted to emulate that
			H.addPoint(x, y + 1);// so this is as similar to the GUI version as possible
			H.addPoint(x + 1, y + 1);
		}
		ArrayList<int[]> inFront = new ArrayList<int[]>();
		for (HoughLine line : H.getLines(houghSensitivity)) {
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
		// TODO check the length of the lines to see which is the most reasonable
		logger.v("Line: " + Integer.toString(inFront.get(0)[0]) + " " + Integer.toString(inFront.get(0)[1]) + " " + Integer.toString(inFront.get(0)[2]) + " " + Integer.toString(inFront.get(0)[3]));
		return inFront.get(0);
	}
	
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
		// if (port.getBytesReceived() < 128) { // LIDAR returns 1980 bytes per cycle
		// logger.v("getBytesReceived", "only " + port.getBytesReceived() + " bytes received");
		// }
		logger.d("Updating LIDAR");
		try {
			for (int i = 0; i < 180; i++) { // Reading in chunks of 4, so only 90 steps
				dists[i] = read(i);
			}
			for (int i = 0; i < 180; i++) { // Do it again for redundancy
				if (dists[i] != 0) dists[i] = read(i);
			}
		}
		catch (Exception e) {
			logger.e("Error updating LIDAR: " + e.getMessage());
		}
	}
	
	public int[] getXY(int i) {
		double angle = i;
		angle = angle * Math.PI / 180;
		int x = (int) (width / 2 + Math.cos(angle + Math.PI / 2) * dists[i] / d);
		int y = (int) (height / 2 + -Math.sin(angle + Math.PI / 2) * dists[i] / d);
		return new int[] {x, y};
	}
	
	public class HoughLine {
		double theta;
		double r;
		static final int greenSensitivityPixels = 5;
		
		public HoughLine(double theta, double r) {
			this.theta = theta;
			this.r = r;
		}
		
		/**
		 * Initialises the hough array. Called by the constructor so you don't need
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
			int houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2;
			// Find edge points and vote in array
			float centerX = width / 2;
			float centerY = height / 2;
			// Draw edges in output array
			double tsin = Math.sin(theta);
			double tcos = Math.cos(theta);
			ArrayList<ArrayList<int[]>> me = new ArrayList<ArrayList<int[]>>();// All the segments in this line
			boolean inLine = false;
			if (theta < Math.PI * 0.25 || theta > Math.PI * 0.75) {
				// Draw vertical-ish lines
				for (int y = 0; y < height; y++) {
					int x = (int) ((r - houghHeight - (y - centerY) * tsin) / tcos + centerX);
					if (x < width && x >= 0) {
						inLine = process(x, y, X, Y, me, inLine);
					}
				}
			} else {
				// Draw horizontal-sh lines
				for (int x = 0; x < width; x++) {
					int y = (int) ((r - houghHeight - (x - centerX) * tcos) / tsin + centerY);
					if (y < height && y >= 0) {
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
				int dist = (X[i] - x) * (X[i] - x) + (Y[i] - y) * (Y[i] - y);
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
		 * Initialises the hough transform. The dimensions of the input image are
		 * needed in order to initialise the hough array.
		 *
		 * @param width The width of the input image
		 * @param height The height of the input image
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
			// Initialise the vector of lines that we'll return
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
						// add the line to the vector
						lines.add(new HoughLine(theta, r));
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