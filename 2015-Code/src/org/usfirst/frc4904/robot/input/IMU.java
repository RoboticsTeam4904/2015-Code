package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc4904.robot.LogKitten;

public class IMU extends MPU9150 {
	private final LogKitten logger;
	private double[] angles; // Angle 0 is perpendicular (yaw), Angle 1 is lateral (pitch), Angle 2 is longitudinal (roll)
	private double[] lastAngles;
	private double[] rate; // Same as above
	private double zeroAngle;
	private double lastTime;
	
	public IMU() {
		// super(serial);
		super();
		logger = new LogKitten("IMU", LogKitten.LEVEL_FATAL, LogKitten.LEVEL_DEBUG);
		angles = new double[3];
		lastAngles = new double[3];
		rate = new double[3];
		Arrays.fill(angles, (double) 0);
		Arrays.fill(lastAngles, (double) 0);
		lastTime = getTime();
		zero();
	}
	
	public byte test() {
		return super.test();
	}
	
	public double turnAngle() {
		return angles[0];
	}
	
	public double[] readRate() {
		logger.v("rate", "" + rate[0]);
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
		if (rate[0] > 0) {
			logger.v("data change", angles[0] + " " + angles[1] + " " + angles[2]);
		}
		lastTime = time;
		lastAngles = angles;
		logger.d("update", Double.toString(angles[0] * 180 / Math.PI));
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