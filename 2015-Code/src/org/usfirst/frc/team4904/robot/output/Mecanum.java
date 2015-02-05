package org.usfirst.frc.team4904.robot.output;


/*
 * Mecanum Drive Library
 * Designed 2014
 * Version: 2.0.1b
 * For FRC or related use only
 * Adapted and modified by team 4904 for 2015
 *
 * Created by AI Robotics #4529
 * www.ai-robotics.com.au
 * info@ai-robotics.com
 *
 * Changelog:
 * Fixed Turn Function - S
 */
import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.input.IMU;
import edu.wpi.first.wpilibj.SpeedController;

public class Mecanum implements Updatable {
	private final SpeedController frontLeftWheel;
	private final SpeedController frontRightWheel;
	private final SpeedController backLeftWheel;
	private final SpeedController backRightWheel;
	private volatile double desiredSpeed;
	private volatile double desiredAngle;
	private volatile double desiredTurnSpeed;
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
		// System.out.println("Backleft" + (backLeft / scaleFactor));
		// System.out.println("Backright" + (backRight / scaleFactor));
		// System.out.println("Frontleft" + (frontLeft / scaleFactor));
		// System.out.println("Frontright" + (frontRight / scaleFactor));
	}
	
	public synchronized void update() {
		// System.out.println("Desired speed: " + desiredTurnSpeed + "\tDesired angle: " + desiredAngle + "\tDisired speed: " + desiredSpeed);
		move(desiredSpeed, desiredAngle, desiredTurnSpeed, absolute); // This system allows for different updating times and rates
	}
	
	public void setDesiredSpeedDirection(double desiredSpeed, double desiredAngle) {
		this.desiredAngle = desiredAngle;
		this.desiredSpeed = desiredSpeed;
	}
	
	public void setDesiredTurnSpeed(double desiredTurnSpeed) {
		this.desiredTurnSpeed = desiredTurnSpeed;
	}

	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}
}