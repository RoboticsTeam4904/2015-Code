package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class MPU9150 implements Updatable {
	private SerialPort port;
	private volatile String imuData;
	// private static final int NUM_LEDS = 209;
	private double[] angles;
	double q[];
	LogKitten logger;
	
	public MPU9150() {
		logger = new LogKitten("MPU9150", LogKitten.LEVEL_FATAL, LogKitten.LEVEL_VERBOSE);
		port = new SerialPort(115200, SerialPort.Port.kMXP);
		port.enableTermination();
		port.setReadBufferSize(8192);
		port.setWriteBufferSize(8192);
		port.reset();
		port.flush();
		imuData = "";
		angles = new double[3];
		Arrays.fill(angles, (double) 0);
		q = new double[4];
	}
	
	public void init() {}
	
	public byte test() {
		return 0x00;
	}
	
	public double[] read() {
		return angles;
	}
	
	private float parseFloat(String data) {
		try {
			byte[] inData = new byte[4];
			data = data.substring(2, 10); // discard the leading "f:"
			return Float.intBitsToFloat(Integer.parseInt(data, 16));
		}
		catch (Exception e) {
			if (data.length() == 10) {
				e.printStackTrace();
			} else {
				logger.w("update", "data: " + data + ", length" + data.length());
			}
			return 0;
		}
	}
	
	public void update() {
		// String current = "a";
		/*
		 * while (!current.matches("\n") && port.getBytesReceived() > 10) { current = port.readString(1); imuData += current; // logger.v("adding data", imuData); }
		 */
		boolean newData = false;
		int br = port.getBytesReceived();
		// logger.v("update", "bytes recieved = " + br);
		while (port.getBytesReceived() > 48) {
			newData = true;
			// logger.v("update", "Bytes received: " + port.getBytesReceived() + " At " + System.currentTimeMillis());
			try {
				imuData = new String(port.read(port.getBytesReceived()));
			}
			catch (Exception e) {
				System.out.println("Error fetching serial data");
				e.printStackTrace();
				return;
			}
			// System.out.println("Got data " + imuData);
			// ///////////////////////////////////////////////////////
			// System.out.println("MPUInput " + imuData);
			if (imuData.length() < 20) {
				logger.w("update", "Too little data");
				return;
			}
			String[] floatString = imuData.split(",");
			if (floatString.length < 4) {
				logger.w("update", "Data array too short :" + floatString.length);
				return;
			}
			double q[] = new double[4];
			q[0] = (double) parseFloat(floatString[0]);
			q[1] = (double) parseFloat(floatString[1]);
			q[2] = (double) parseFloat(floatString[2]);
			q[3] = (double) parseFloat(floatString[3]);
			angles[0] = Math.atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0] * q[0] + 2 * q[1] * q[1] - 1);
			angles[1] = -1 * Math.asin(2 * q[1] * q[3] + 2 * q[0] * q[2]);
			angles[2] = Math.atan2(2 * q[2] * q[3] - 2 * q[0] * q[1], 2 * q[0] * q[0] + 2 * q[3] * q[3] - 1);
			logger.v("update", "update1 " + Double.toString(angles[0]));
			// logger.v("update", "update2 " + Double.toString(angles[1]));
			// logger.v("update", "update3 " + Double.toString(angles[2]));
		}
		if (newData) {
			port.flush();
			port.reset();
		}
	}
}
