package org.usfirst.frc4904.robot.input;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;

public class UDAR implements Updatable {
	private final SuperSerial serial;
	private static final int NUM_SENSORS = 3;
	private double[] data;
	LogKitten logger;
	
	public UDAR(SuperSerial serial) {
		this.serial = serial;
		data = new double[3];
		logger = new LogKitten("UDAR", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_VERBOSE);
	}
	
	private int bytesCurrentlyAvailable() {
		return serial.availableUDARData();
	}
	
	public double[] read() {
		return data;
	}
	
	public void update() {
		if (bytesCurrentlyAvailable() < NUM_SENSORS * 2) {
			String[] tmpData;
			tmpData = serial.readUDAR(NUM_SENSORS * 2).split(" ");
			if (tmpData.length < 3) {
				logger.w("update", "Not enough data points");
			}
			for (int i = 0; i < NUM_SENSORS; i++) {
				data[i] = Integer.parseInt(tmpData[i]);
			}
		}
	}
}