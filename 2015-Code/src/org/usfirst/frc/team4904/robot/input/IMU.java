package org.usfirst.frc.team4904.robot.input;


import java.util.Arrays;
import java.util.Date;
import org.usfirst.frc.team4904.robot.LogKitten;
import org.usfirst.frc.team4904.robot.Robot;
import org.usfirst.frc.team4904.robot.Updatable;

public class IMU implements Updatable {
	private MPU9150 mpu9150;
	public double[] rawData = new double[10];
	private double startTime;
	private long updates = 0;
	private LogKitten logger;
	
	public IMU() {
		mpu9150 = new MPU9150();
		mpu9150.init();
		zero();
		logger = new LogKitten("IMU", LogKitten.LEVEL_DEBUG);
	}
	
	public int test() {
		return mpu9150.test();
	}
	
	public double getAngle() {
		// TODO return current robot angle relative to beginning of match (0 - 2pi)
		return 0D;
	}
	
	public void zero() {
		// TODO set current orientation as "forward"
		update();
		Date date = new java.util.Date();
		startTime = Robot.time();
		updates = 0;
	}
	
	public synchronized void update() {
		mpu9150.update();
		readData();
		updateKalman();
	}
	
	private void readData() {
		updates++;
		// TODO only read data if enough data is available, otherwise return so
		// that this function is always fast
		rawData = mpu9150.read();
		if (updates % 30 == 0) {
			logger.d("readData", updates / (Robot.time() - startTime) + " hz ");
			logger.d("readData", Arrays.toString(rawData));
		}
	}
	
	private void updateKalman() {}
	
	public void disable() {}
}