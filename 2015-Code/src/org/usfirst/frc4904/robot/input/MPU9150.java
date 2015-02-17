package org.usfirst.frc4904.robot.input;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;

public class MPU9150 implements Updatable {
	private double[] angles;
	private final SuperSerial serial;
	LogKitten logger;
	
	public MPU9150(SuperSerial serial) {
		this.serial = serial;
		logger = new LogKitten("MPU9150", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_VERBOSE);
		angles = new double[3];
		for (int i = 0; i < 3; i++) {
			angles[i] = 0;
		}
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
			inData[1] = (byte) Byte.parseByte(data.substring(2, 4));
			inData[2] = (byte) Byte.parseByte(data.substring(4, 6));
			inData[3] = (byte) Byte.parseByte(data.substring(6, 8));
			int intbits = (inData[3] << 24) | ((inData[2] & 0xff) << 16) | ((inData[1] & 0xff) << 8) | (inData[0] & 0xff);
			return Float.intBitsToFloat(intbits);
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void update() {
		if (serial.availableIMUData() < 10) {
			logger.v("MPU", "data too small");
			return;
		}
		String[] floatString = serial.readIMU(0).split(",");
		double q[] = new double[4];
		q[0] = (double) parseFloat(floatString[0]);
		q[1] = (double) parseFloat(floatString[1]);
		q[2] = (double) parseFloat(floatString[2]);
		q[3] = (double) parseFloat(floatString[3]);
		angles[0] = Math.atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0] * q[0] + 2 * q[1] * q[1] - 1);
		angles[1] = -1 * Math.asin(2 * q[1] * q[3] + 2 * q[0] * q[2]);
		angles[2] = Math.atan2(2 * q[2] * q[3] - 2 * q[0] * q[1], 2 * q[0] * q[0] + 2 * q[3] * q[3] - 1);
		logger.v("update", Double.toString(angles[0]));
		logger.v("update", Double.toString(angles[1]));
		logger.v("update", Double.toString(angles[2]));
	}
}
