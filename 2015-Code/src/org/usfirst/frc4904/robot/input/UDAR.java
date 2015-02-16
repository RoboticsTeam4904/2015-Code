package org.usfirst.frc4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc4904.robot.Updatable;

public class UDAR implements Updatable {
	private final SuperSerial serial;
	private static final int NUM_SENSORS = 3;
	private double[] data;
	
	public UDAR(SuperSerial serial) {
		this.serial = serial;
		data = new double[3];
	}
	
	private int bytesCurrentlyAvailable() {
		return serial.availableUDARBytes();
	}
	
	public double[] read() {
		return data;
	}
	
	public void update() {
		if (bytesCurrentlyAvailable() < NUM_SENSORS * 2) {
			byte[] tmpData = new byte[NUM_SENSORS];
			tmpData = serial.readUDAR(NUM_SENSORS * 2);
			for (int i = 0; i < NUM_SENSORS; i++) {
				data[i] = new BigInteger(new byte[] {0, tmpData[2 * i], tmpData[2 * i + 1]}).intValue();
			}
		}
	}
}