package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.input.IMU;
import edu.wpi.first.wpilibj.SpeedController;

public class Mecanum implements Updatable {
	private final SpeedController frontLeftWheel;
	private final SpeedController frontRightWheel;
	private final SpeedController backLeftWheel;
	private final SpeedController backRightWheel;
	private volatile double currentXSpeed;
	private volatile double currentYSpeed;
	private volatile double currentTurnSpeed;
	private volatile double previousXSpeed;
	private volatile double previousYSpeed;
	private volatile double previousTurnSpeed;
	private volatile double previousTime;
	private volatile boolean absolute;
	private final IMU imu;
	
	public Mecanum(SpeedController frontLeftWheel, SpeedController frontRightWheel, SpeedController backLeftWheel, SpeedController backRightWheel, IMU imu) {
		// Initialize motor controllers with default ports
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
		this.imu = imu;
	}
	
	private void move(double desiredSpeed, double desiredAngle, double turnSpeed, boolean absolute) {
		// @param desiredSpeed double between 0 and 1 specifying wanted movement speed
		// @param desiredAngle double between 0 and 2pi specifying wanted movement angle in radians
		// @param turnSpeed double between 0 and 1 specifying rotational speed
		// @param absolute boolean specifying whether to move relative to the robot or absolutely (relative to the compass)
		double workingAngle;
		if (absolute) {
			workingAngle = (desiredAngle - imu.getAngle()) % (Math.PI * 2);
		} else {
			workingAngle = desiredAngle;
		}
		double frontLeft = desiredSpeed * Math.sin(workingAngle + Math.PI / 4) + turnSpeed;
		double frontRight = desiredSpeed * Math.cos(workingAngle + Math.PI / 4) - turnSpeed;
		double backLeft = desiredSpeed * Math.cos(workingAngle + Math.PI / 4) + turnSpeed;
		double backRight = desiredSpeed * Math.sin(workingAngle + Math.PI / 4) - turnSpeed;
		double scaleFactor = Math.max(Math.max(Math.max(Math.abs(frontLeft), Math.abs(frontRight)), Math.abs(backLeft)), Math.abs(backRight));
		if (scaleFactor < 1) {
			scaleFactor = 1;
		}
		frontLeftWheel.set(frontLeft / scaleFactor); // Negated because of motors being of the inside of chassis
		frontRightWheel.set(-frontRight / scaleFactor);
		backLeftWheel.set(backLeft / scaleFactor); // Negated because of motors being of the inside of chassis
		backRightWheel.set(-backRight / scaleFactor);
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
	
	public synchronized void update() {
		double currentTime = time();
		double timeDifference = currentTime - previousTime;
		double newXSpeed = adjust(previousXSpeed, currentXSpeed, 16 * timeDifference);
		double newYSpeed = adjust(previousYSpeed, currentYSpeed, 16 * timeDifference);
		double newTurnSpeed = adjust(previousTurnSpeed, currentTurnSpeed, timeDifference);
		previousXSpeed = newXSpeed;
		previousYSpeed = newYSpeed;
		previousTurnSpeed = newTurnSpeed;
		previousTime = currentTime;
		double currentSpeed = Math.sqrt(newXSpeed * newXSpeed + newYSpeed * newYSpeed);
		double currentAngle = Math.atan2(newYSpeed, newXSpeed);
		move(currentSpeed, currentAngle, newTurnSpeed, absolute); // This system allows for different updating times and rates
	}
	
	public static double adjust(double prevValue, double currentValue, double maxChange) {// Dampen or don't dampen
		if (currentValue < prevValue) { // Deaccelerating
			return Math.max(currentValue, prevValue - maxChange);
		} else { // Accelerating
			return Math.min(currentValue, prevValue + maxChange);
		}
	}
	
	public void setDesiredXYSpeed(double desiredXSpeed, double desiredYSpeed) {
		currentXSpeed = desiredXSpeed;
		currentYSpeed = desiredYSpeed;
	}
	
	public void setDesiredTurnSpeed(double desiredTurnSpeed) {
		currentTurnSpeed = desiredTurnSpeed;
	}
	
	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}
}