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
	private volatile double currentSpeed;
	private volatile double currentAngle;
	private volatile double currentTurnSpeed;
	private volatile double previousSpeed;
	private volatile double previousAngle;
	private volatile double previousTurnSpeed;
	private volatile double previousTime;
	private volatile boolean absolute;
	public static final double maxAccel = 2;// Maximum motor output change per second
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
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
	
	public synchronized void update() {
		double currentTime = time();
		double timeDifference = currentTime - previousTime;
		double maxChange = maxAccel * timeDifference;
		double newSpeed = adjust(previousSpeed, currentSpeed, maxChange);
		double newAngle = adjust(previousAngle, currentAngle, maxChange);
		double newTurnSpeed = adjust(previousTurnSpeed, currentTurnSpeed, maxChange);
		previousSpeed = currentSpeed;
		previousAngle = currentAngle;
		previousTurnSpeed = currentTurnSpeed;
		previousTime = currentTime;
		currentSpeed = newSpeed;
		currentAngle = newAngle;
		currentTurnSpeed = newTurnSpeed;
		// TODO SPEED CONTROLLING GOES HERE
		// System.out.println("Desired speed: " + desiredTurnSpeed + "\tDesired angle: " + desiredAngle + "\tDisired speed: " + desiredSpeed);
		move(currentSpeed, currentAngle, currentTurnSpeed, absolute); // This system allows for different updating times and rates
	}

	public static double adjust(double prevValue, double currentValue, double maxChange) {
		if (currentValue < prevValue) {
			if (prevValue - currentValue < maxChange) {
				return currentValue;
			} else {
				return prevValue - maxChange;
			}
		} else {
			if (currentValue - prevValue < maxChange) {
				return currentValue;
			} else {
				return prevValue + maxChange;
			}
		}
	}

	public void setDesiredSpeedDirection(double desiredSpeed, double desiredAngle) {
		this.currentAngle = desiredAngle;
		this.currentSpeed = desiredSpeed;
	}

	public void setDesiredTurnSpeed(double desiredTurnSpeed) {
		this.currentTurnSpeed = desiredTurnSpeed;
	}
	
	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}
}