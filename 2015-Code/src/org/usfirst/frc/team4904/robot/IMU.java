package org.usfirst.frc.team4904.robot;

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
	
	public void update() {
		this.readData();
		this.updateKalman();
	}
	
	private void readData() {
		
	}
	
	private void updateKalman() {
		
	}
}
