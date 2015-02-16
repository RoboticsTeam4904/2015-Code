package org.usfirst.frc4904.robot.input;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;

public class MPU9150 implements Updatable {
	private double movementMagnitude;
	private double movementAngle;
	private double absoluteAngle;
	private final SuperSerial serial;
	LogKitten logger;
	
	public MPU9150(SuperSerial serial) {
		this.serial = serial;
		logger = new LogKitten("MPU9150", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_VERBOSE);
	}
	
	public void init() {}
	
	public byte test() {
		return 0x00;
	}
	
	public double[] read() {
		return new double[] {movementMagnitude, movementAngle, absoluteAngle};
	}
	
	public void update() {
		String data = serial.readIMU(0);
		logger.v("update", data);
	}
}
