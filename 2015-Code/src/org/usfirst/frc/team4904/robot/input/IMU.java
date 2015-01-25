package org.usfirst.frc.team4904.robot.input;

import org.usfirst.frc.team4904.robot.Updatable;

public class IMU implements Updatable{
	public IMU() {
		this.zero();
	}
	
	public float getAngle() {
		// TODO return current robot angle relative to zero point (0 - 360)
		return 0F;
	}
	
	public void zero() {
		// TODO set current orientation as "forward"
	}
	
	public synchronized void update() {
		this.readData();
		this.updateKalman();
	}
	
	private void readData() {
		// TODO only read data if enough data is available, otherwise return so that this function is always fast
	}
	
	private void updateKalman() {
		
	}
}