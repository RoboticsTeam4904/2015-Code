package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.IMU;
import edu.wpi.first.wpilibj.PIDController;

public class Mecanum implements Updatable, Disablable {
	private final DampenedMotor frontLeftWheel;
	private final DampenedMotor frontRightWheel;
	private final DampenedMotor backLeftWheel;
	private final DampenedMotor backRightWheel;
	private final PIDController pid;
	private final PIDVariable turnSpeed;
	private volatile double desiredXSpeed;
	private volatile double desiredYSpeed;
	private volatile double desiredTurnSpeed;
	private volatile double tsAdjust; // Turn speed adjust
	private volatile boolean absolute;
	private final IMU imu;
	private final LogKitten logger;
	private final boolean overridePID = false;
	
	public Mecanum(DampenedMotor frontLeftWheel, DampenedMotor frontRightWheel, DampenedMotor backLeftWheel, DampenedMotor backRightWheel, IMU imu) {
		// Initialize motor controllers with default ports
		logger = new LogKitten("Mecanum", LogKitten.LEVEL_FATAL, LogKitten.LEVEL_VERBOSE);
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
		this.imu = imu;
		this.turnSpeed = new PIDVariable();
		pid = new PIDController(-1.25F / 180F, -0.001F / 180F, 0, imu, turnSpeed);
		if (!overridePID) {
			pid.setContinuous();
			pid.setInputRange(0, 360);
			pid.setOutputRange(-1, 1);
			pid.setAbsoluteTolerance(0.5);
		}
		tsAdjust = 0;
	}
	
	public void enable() {
		if (!overridePID) {
			pid.enable();
		} else {
			pid.disable();
		}
	}
	
	public void disable() {
		pid.disable();
	}
	
	private void move(double desiredSpeed, double desiredAngle, double turnSpeed, boolean absolute) {
		// @param desiredSpeed double between 0 and 1 specifying wanted movement speed
		// @param desiredAngle double between 0 and 2pi specifying wanted movement angle in radians
		// @param turnSpeed double between 0 and 1 specifying rotational speed
		// @param absolute boolean specifying whether to move relative to the robot or absolutely (relative to the compass)
		double workingAngle;
		if (absolute) {
			workingAngle = (desiredAngle - imu.read()[0]) % (Math.PI * 2);
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
		double setSpeed = Math.sqrt(desiredXSpeed * desiredXSpeed + desiredYSpeed * desiredYSpeed);
		double setAngle = Math.atan2(desiredYSpeed, desiredXSpeed);
		if (!overridePID) {
			pid.setSetpoint(desiredTurnSpeed);
		} else {
			turnSpeed.pidWrite(desiredTurnSpeed);
		}
		move(setSpeed, setAngle, turnSpeed.read(), absolute); // This system allows for different updating times and rates
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