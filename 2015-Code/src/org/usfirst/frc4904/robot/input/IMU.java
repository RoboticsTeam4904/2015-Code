package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;

public class IMU extends MPU9150 implements Updatable {
	private final LogKitten logger;
	private double[] angles; // Angle 0 is perpendicular (yaw), Angle 1 is lateral (pitch), Angle 2 is longitudinal (roll)
	private double[] lastAngles;
	private double[] rate; // Same as above
	private double zeroAngle;
	private double lastTime;
	
	public IMU(SuperSerial serial) {
		super(serial);
		logger = new LogKitten("IMU", LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_VERBOSE);
		angles = new double[3];
		lastAngles = new double[3];
		for (int i = 0; i < 3; i++) {
			angles[i] = 0;
			lastAngles[i] = 0;
		}
		zero();
		lastTime = getTime();
	}
	
	public byte test() {
		return super.test();
	}
	
	public double turnAngle() {
		return angles[0];
	}
	
	public double[] readRate() {
		return rate;
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
		for (int i = 0; i < 3; i++) {
			rate[i] = (angles[i] - lastAngles[i]) / (time - lastTime);
		}
		lastTime = time;
		lastAngles = angles;
	}
	
	private void readData() {
		angles = super.read();
		logger.d("readData", Arrays.toString(angles));
	}
	
	private double getTime() {
		return System.currentTimeMillis();
	}
	
	public boolean isGoingOverScoringPlatform() {
		double[] eulers = read();
		double angle = eulers[1];
		return angle > 5 * Math.PI / 180; // TODO 5 degrees is ballpark - measure
	}
}