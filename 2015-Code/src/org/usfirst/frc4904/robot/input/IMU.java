package org.usfirst.frc4904.robot.input;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;

public class IMU extends MPU9150 implements Updatable {
	private long updates = 0;
	private final LogKitten logger;
	private double currentSpeed;
	private double currentAngle;
	private double currentTurnSpeed;
	private double zeroAngle;
	
	public IMU(SuperSerial serial) {
		super(serial);
		zero();
		logger = new LogKitten("IMU", LogKitten.LEVEL_DEBUG);
		currentSpeed = 0;
		currentAngle = 0;
		currentTurnSpeed = 0;
	}
	
	public byte test() {
		return super.test();
	}
	
	public double getAngle() {
		// TODO return current robot angle relative to beginning of match (0 - 2pi)
		return 0D;
	}
	
	public void zero() {
		// TODO set current orientation as "forward"
		zeroAngle = super.read()[2];
		update();
		updates = 0;
	}
	
	public synchronized void update() {
		super.update();
		readData();
		updateKalman();
	}
	
	public double[] getMovement() {
		return new double[] {currentSpeed, currentAngle, currentTurnSpeed};
	}
	
	private void readData() {
		updates++;
		// TODO only read data if enough data is available, otherwise return so
		// that this function is always fast
		double[] rawData = super.read();
		if (updates % 30 == 0) {
			// logger.d("readData", updates / (Robot.time() - startTime) + " hz ");
			// logger.d("readData", Arrays.toString(rawData));
		}
	}
	
	private void updateKalman() {}
}