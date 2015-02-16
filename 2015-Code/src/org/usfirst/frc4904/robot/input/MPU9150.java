package org.usfirst.frc4904.robot.input;

import org.usfirst.frc4904.robot.Updatable;


public class MPU9150 implements Updatable {
	private double movementMagnitude;
	private double movementAngle;
	private double absoluteAngle;
	private final SuperSerial serial;
	
	public MPU9150(SuperSerial serial) {
		this.serial = serial;
	}
	
	public void init() {}
	
	public byte test() {
		return 0x00;
	}
	
	public double[] read() {
		return new double[] {movementMagnitude, movementAngle, absoluteAngle};
	}
	
	public void update() {
		byte[] data = serial.readIMU(0);
	}
}
