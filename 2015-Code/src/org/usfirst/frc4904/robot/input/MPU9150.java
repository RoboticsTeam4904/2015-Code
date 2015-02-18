package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class MPU9150 implements Updatable {
	private final SerialPort port;
	private volatile String imuData;
	private static final int NUM_LEDS = 209;
	private double[] angles;
	double q[];
	LogKitten logger;
	
	public MPU9150() {
		logger = new LogKitten("MPU9150", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_VERBOSE);
		port = new SerialPort(115200, SerialPort.Port.kMXP);
		port.enableTermination();
		port.setReadBufferSize(100000);
		port.reset();
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
			inData[1] = (byte) Integer.parseInt(data.substring(2, 4), 16);
			inData[2] = (byte) Integer.parseInt(data.substring(4, 6), 16);
			inData[3] = (byte) Integer.parseInt(data.substring(6, 8), 16);
			int intbits = (inData[3] << 24) | ((inData[2] & 0xff) << 16) | ((inData[1] & 0xff) << 8) | (inData[0] & 0xff);
			return Float.intBitsToFloat(intbits);
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void update() {
		String dataString = "";
		// String current = "a";
		/*
		 * while (!current.matches("\n") && port.getBytesReceived() > 10) { current = port.readString(1); dataString += current; // logger.v("adding data", dataString); }
		 */
		if (port.getBytesReceived() < 100) {
			return;
		}
		try {
			dataString = port.readString();
		}
		catch (Exception e) {
			return;
		}
		logger.v("update", "Got data " + dataString);
		imuData = dataString;
		// ///////////////////////////////////////////////////////
		logger.v("MPUInput", dataString);
		if (dataString.length() < 20) {
			logger.v("update", "Too little data");
			return;
		}
		dataString = dataString.substring(3);
		String[] floatString = dataString.split(",");
		if (floatString.length < 4) {
			logger.v("update", "Data array too short :" + floatString.length);
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
		logger.v("update1", Double.toString(angles[0]));
		logger.v("update2", Double.toString(angles[1]));
		logger.v("update3", Double.toString(angles[2]));
	}
}
