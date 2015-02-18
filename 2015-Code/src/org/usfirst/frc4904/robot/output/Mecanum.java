package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.IMU;

public class Mecanum implements Updatable {
	private final DampenedMotor frontLeftWheel;
	private final DampenedMotor frontRightWheel;
	private final DampenedMotor backLeftWheel;
	private final DampenedMotor backRightWheel;
	private volatile double desiredXSpeed;
	private volatile double desiredYSpeed;
	private volatile double desiredTurnSpeed;
	private volatile double tsAdjust; // Turn speed adjust
	private final PID turnSpeedPID;
	private volatile boolean absolute;
	private final IMU imu;
	private final LogKitten logger;
	
	public Mecanum(DampenedMotor frontLeftWheel, DampenedMotor frontRightWheel, DampenedMotor backLeftWheel, DampenedMotor backRightWheel, IMU imu) {
		// Initialize motor controllers with default ports
		logger = new LogKitten("Mecanum", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_VERBOSE);
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
		this.imu = imu;
		this.turnSpeedPID = new PID(1, 1, 1);
		tsAdjust = 0;
	}
	
	public void train() {
		turnSpeedPID.train();
	}
	
	private void move(double desiredSpeed, double desiredAngle, double turnSpeed, boolean absolute) {
		// @param desiredSpeed double between 0 and 1 specifying wanted movement speed
		// @param desiredAngle double between 0 and 2pi specifying wanted movement angle in radians
		// @param turnSpeed double between 0 and 1 specifying rotational speed
		// @param absolute boolean specifying whether to move relative to the robot or absolutely (relative to the compass)
		double workingAngle;
		if (absolute) {
			workingAngle = (desiredAngle - imu.turnAngle()) % (Math.PI * 2);
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
		frontLeftWheel.setValue(frontLeft / scaleFactor); // Negated because of motors being of the inside of chassis
		frontRightWheel.setValue(-frontRight / scaleFactor);
		backLeftWheel.setValue(backLeft / scaleFactor); // Negated because of motors being of the inside of chassis
		backRightWheel.setValue(-backRight / scaleFactor);
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
	
	public synchronized void update() {
		double turnSpeed = imu.readRate()[0];
		logger.v("turnSpeed", "" + turnSpeed);
		double setSpeed = Math.sqrt(desiredXSpeed * desiredXSpeed + desiredYSpeed * desiredYSpeed);
		double setAngle = Math.atan2(desiredYSpeed, desiredXSpeed);
		turnSpeed = desiredTurnSpeed;
		// turnSpeed = turnSpeedPID.calculate(desiredTurnSpeed, turnSpeed);
		move(setSpeed, setAngle, turnSpeed, absolute); // This system allows for different updating times and rates
	}
	
	public void setDesiredXYSpeed(double desiredXSpeed, double desiredYSpeed) {
		this.desiredXSpeed = desiredYSpeed;
		this.desiredYSpeed = desiredXSpeed;
	}
	
	public void setDesiredTurnSpeed(double desiredTurnSpeed) {
		this.desiredTurnSpeed = desiredTurnSpeed;
	}
	
	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}
}