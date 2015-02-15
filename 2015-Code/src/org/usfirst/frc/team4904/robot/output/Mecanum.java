package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.input.IMU;

public class Mecanum implements Updatable {
	private final DampenedMotor frontLeftWheel;
	private final DampenedMotor frontRightWheel;
	private final DampenedMotor backLeftWheel;
	private final DampenedMotor backRightWheel;
	private volatile double desiredXSpeed;
	private volatile double desiredYSpeed;
	private volatile double desiredTurnSpeed;
	private final PID speedPID;
	private final PID anglePID;
	private final PID turnSpeedPID;
	private volatile boolean absolute;
	private final IMU imu;
	
	public Mecanum(DampenedMotor frontLeftWheel, DampenedMotor frontRightWheel, DampenedMotor backLeftWheel, DampenedMotor backRightWheel, IMU imu) {
		// Initialize motor controllers with default ports
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
		this.imu = imu;
		this.speedPID = new PID(0.1, 0.1, 0.1);
		this.anglePID = new PID(0.1, 0.1, 0.1);
		this.turnSpeedPID = new PID(0.1, 0.1, 0.1);
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
		frontLeftWheel.setValue(frontLeft / scaleFactor); // Negated because of motors being of the inside of chassis
		frontRightWheel.setValue(-frontRight / scaleFactor);
		backLeftWheel.setValue(backLeft / scaleFactor); // Negated because of motors being of the inside of chassis
		backRightWheel.setValue(-backRight / scaleFactor);
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
	
	public synchronized void update() {
		double[] movement = imu.getMovement();
		double setSpeed = Math.sqrt(desiredXSpeed * desiredXSpeed + desiredYSpeed * desiredYSpeed);
		double setAngle = Math.atan2(desiredYSpeed, desiredXSpeed);
		double setTurnSpeed = desiredTurnSpeed;
		// setSpeed = speedPID.calculate(setSpeed, movement[0]);
		// setAngle = anglePID.calculate(setAngle, movement[1]);
		// setTurnSpeed = turnSpeedPID.calculate(setTurnSpeed, movement[2]);
		move(setSpeed, setAngle, setTurnSpeed, absolute); // This system allows for different updating times and rates
	}
	
	public void setDesiredXYSpeed(double desiredXSpeed, double desiredYSpeed) {
		this.desiredXSpeed = desiredXSpeed;
		this.desiredYSpeed = desiredYSpeed;
	}
	
	public void setDesiredTurnSpeed(double desiredTurnSpeed) {
		this.desiredTurnSpeed = desiredTurnSpeed;
	}
	
	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}
}