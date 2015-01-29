package org.usfirst.frc.team4904.robot.input;


import org.usfirst.frc.team4904.robot.Updatable;

public class IMU implements Updatable {
	private MPU9150 mpu9150;
	private int[] rawData = new int[10];

	public IMU() {
		this.mpu9150 = new MPU9150();
		this.zero();
	}

	public float getAngle() {
		// TODO return current robot angle relative to zero point (0 - 360)
		return 0F;
	}

	public void zero() {
		// TODO set current orientation as "forward"
		this.update();
	}

	public synchronized void update() {
		this.readData();
		this.updateKalman();
	}

	private void readData() {
		// TODO only read data if enough data is available, otherwise return so
		// that this function is always fast
		this.rawData = this.mpu9150.read();
	}

	private void updateKalman() {}
}