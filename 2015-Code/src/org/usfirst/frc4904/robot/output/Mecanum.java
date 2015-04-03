package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Enablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.IMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Mecanum implements Updatable, Disablable, Enablable {
	private final DampenedMotor frontLeftWheel;
	private final DampenedMotor frontRightWheel;
	private final DampenedMotor backLeftWheel;
	private final DampenedMotor backRightWheel;
	private final DisablablePID pid;
	private final PIDVariable turnSpeed;
	private final IMU imu;
	private volatile double desiredXSpeed;
	private volatile double desiredYSpeed;
	private volatile double desiredTurnSpeed;
	private volatile double desiredAngle;
	private volatile double actualTurnSpeed;
	private final LogKitten logger;
	private static final int INPUT_RESCALE = 8;
	
	public Mecanum(DampenedMotor frontLeftWheel, DampenedMotor frontRightWheel, DampenedMotor backLeftWheel, DampenedMotor backRightWheel, IMU imu, double Kp, double Ki, double Kd) {
		// Initialize motor controllers with default ports
		logger = new LogKitten();
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
		this.turnSpeed = new PIDVariable();
		this.imu = imu;
		pid = new DisablablePID(Kp, Ki, Kd, imu, turnSpeed, true);
		pid.setContinuous();
		pid.setInputRange(0, 360);
		pid.setOutputRange(-1, 1);
		pid.setAbsoluteTolerance(0.5);
		// Add SmartDashboard fields for PID constants
		SmartDashboard.putNumber("Kp Mecanum", 0.003);
		SmartDashboard.putNumber("Ki Mecanum", 0);
		SmartDashboard.putNumber("Kd Mecanum", 0);
		actualTurnSpeed = 0;
		desiredAngle = imu.getYaw();
	}
	
	public void enable() {
		pid.setSetpoint(imu.getYaw());
		setDesiredAngle(imu.getYaw());
		actualTurnSpeed = 0;
		setDesiredTurnSpeed(0);
		pid.enable();
	}
	
	public void disable() {
		pid.reset();
		pid.setSetpoint(imu.getYaw());
		setDesiredAngle(imu.getYaw());
		actualTurnSpeed = 0;
		setDesiredTurnSpeed(0);
	}
	
	public void zero() {
		imu.zero();
		pid.setSetpoint(imu.getYaw());
	}
	
	private void move(double desiredSpeed, double desiredAngle, double turnSpeed) {
		// @param desiredSpeed double between 0 and 1 specifying wanted movement speed
		// @param desiredAngle double between 0 and 2pi specifying wanted movement angle in radians
		// @param turnSpeed double between 0 and 1 specifying rotational speed
		double frontLeft = desiredSpeed * Math.sin(desiredAngle + Math.PI / 4) + turnSpeed;
		double frontRight = desiredSpeed * Math.cos(desiredAngle + Math.PI / 4) - turnSpeed;
		double backLeft = desiredSpeed * Math.cos(desiredAngle + Math.PI / 4) + turnSpeed;
		double backRight = desiredSpeed * Math.sin(desiredAngle + Math.PI / 4) - turnSpeed;
		double scaleFactor = Math.max(Math.max(Math.max(Math.abs(frontLeft), Math.abs(frontRight)), Math.abs(backLeft)), Math.abs(backRight));
		if (scaleFactor < 1) {
			scaleFactor = 1;
		}
		frontLeftWheel.setValue(frontLeft / scaleFactor);
		frontRightWheel.setValue(-frontRight / scaleFactor); // Negated because of motors being of the inside of chassis
		backLeftWheel.setValue(backLeft / scaleFactor);
		backRightWheel.setValue(-backRight / scaleFactor); // Negated because of motors being of the inside of chassis
	}
	
	public synchronized void update() {
		SmartDashboard.putNumber("Turn Speed PID Output", turnSpeed.read());
		pid.setPID(SmartDashboard.getNumber("Kp Mecanum"), SmartDashboard.getNumber("Ki Mecanum") / 100, SmartDashboard.getNumber("Kd Mecanum"));
		double setSpeed = Math.sqrt(desiredXSpeed * desiredXSpeed + desiredYSpeed * desiredYSpeed);
		double setAngle = Math.atan2(desiredYSpeed, desiredXSpeed);
		if (pid.isEnable()) {
			pid.setSetpoint(desiredAngle);
			actualTurnSpeed = turnSpeed.read();
			// System.out.println(actualTurnSpeed + " | " + imu.pidGet() + " | " + pid.getSetpoint());
		} else {
			actualTurnSpeed = desiredTurnSpeed;
			System.out.println("Actual: " + actualTurnSpeed);
		}
		move(setSpeed, setAngle, actualTurnSpeed); // This system allows for different updating times and rates
	}
	
	public void setDesiredXYSpeed(double desiredXSpeed, double desiredYSpeed) {
		this.desiredXSpeed = desiredYSpeed;
		this.desiredYSpeed = desiredXSpeed;
	}
	
	public void setDesiredAngle(double desiredAngle) {
		this.desiredAngle = ((desiredAngle % 360) + 360) % 360;
	}
	
	public void setDesiredTurnSpeed(double turnSpeed) {
		this.desiredTurnSpeed = desiredTurnSpeed;
	}
}
