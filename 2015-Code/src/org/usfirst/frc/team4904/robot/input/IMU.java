package org.usfirst.frc.team4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc.team4904.robot.Updatable;

public class IMU implements Updatable {
	private MPU9150 mpu9150;
	public double[] rawData = new double[10];
	private java.util.Date startTime;
	private long updates = 0;

	public IMU() {
		mpu9150 = new MPU9150();
		mpu9150.init();
		zero();
	}

	public float getAngle() {
		// TODO return current robot angle relative to zero point (0 - 360)
		return 0F;
	}

	public void zero() {
		// TODO set current orientation as "forward"
		update();
		this.startTime = new java.util.Date();
		this.updates = 0;
	}

	public synchronized void update() {
		readData();
		updateKalman();
	}

	private void readData() {
		this.updates++;
		// TODO only read data if enough data is available, otherwise return so
		// that this function is always fast
		rawData = this.mpu9150.read();
		if (this.updates % 1000 == 0) {
			System.out.println(Arrays.toString(this.rawData));
		}
	}

	private void updateKalman() {}
}