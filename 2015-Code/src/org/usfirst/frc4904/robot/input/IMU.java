package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;

public class IMU extends MPU9150 implements Updatable {
	private final LogKitten logger;
	private double[] angles; // Angle 0 is perpendicular (yaw), Angle 1 is lateral (pitch), Angle 2 is longitudinal (roll)
	private double[] speed; // Same as above
	private double zeroAngle;
	private double lastTime;
	
	public IMU(SuperSerial serial) {
		super(serial);
		zero();
		logger = new LogKitten("IMU", LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_VERBOSE);
		angles = new double[3];
		lastTime = getTime();
	}
	
	public byte test() {
		return super.test();
	}
	
	public double turnAngle() {
		return angles[0];
	}
	
	private void zero() {
		// TODO set current orientation as "forward"
		update();
		zeroAngle = angles[0];
	}
	
	public synchronized void update() {
		double time = getTime();
		super.update();
		readData();
		lastTime = time;
	}
	
	private void readData() {
		angles = super.read();
		logger.d("readData", Arrays.toString(angles));
	}
	
	private double getTime() {
		return System.currentTimeMillis();
	}
}