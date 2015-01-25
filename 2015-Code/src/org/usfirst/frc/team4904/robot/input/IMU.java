package org.usfirst.frc.team4904.robot.input;


import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.I2C;

public class IMU implements Updatable {
	private I2C i2c;
	
	public IMU(I2C i2c) {
		this.i2c = i2c; // Initialize I2C
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
	}
	
	private void updateKalman() {}
}