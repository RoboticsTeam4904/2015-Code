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

import edu.wpi.first.wpilibj.SpeedController;

public class Mecanum implements Updatable{
	
	private final SpeedController frontLeftWheel;
	private final SpeedController frontRightWheel;
	private final SpeedController backLeftWheel;
	private final SpeedController backRightWheel;
	
	private volatile double desiredSpeed;
	private volatile double desiredAngle;
	private volatile double desiredTurnSpeed;
	
	public Mecanum(SpeedController frontLeftWheel, SpeedController frontRightWheel, SpeedController backLeftWheel, SpeedController backRightWheel){
		// Initialize motor controllers with default ports
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
	}
	
	
	private void move(double desiredSpeed, double desiredAngle, double turnSpeed) {
		// @param	desiredSpeed	double between 0 and 1 specifying wanted movement speed
		// @param	desiredAngle	double between 0 and 2pi specifying wanted movement angle in radians
		// @param	turnSpeed		double between 0 and 1 specifying rotational speed
		
		double frontLeft = desiredSpeed * Math.sin(desiredAngle + Math.PI / 4) + turnSpeed;
		double frontRight = desiredSpeed * Math.cos(desiredAngle + Math.PI / 4) - turnSpeed;
		double backLeft = desiredSpeed * Math.cos(desiredAngle + Math.PI / 4) + turnSpeed;
		double backRight = desiredSpeed * Math.sin(desiredAngle + Math.PI / 4) - turnSpeed;

		double scaleFactor = Math.max(Math.max(Math.max(Math.abs(frontLeft), Math.abs(frontRight)), Math.abs(backLeft)), Math.abs(backRight));

		frontLeftWheel.set(frontLeft / scaleFactor);
		frontRightWheel.set(frontRight / scaleFactor);
		backLeftWheel.set(backLeft / scaleFactor);
		backRightWheel.set(backRight / scaleFactor);
	}
	
	public synchronized void update() {
		this.move(this.desiredSpeed, this.desiredAngle, this.desiredTurnSpeed);//This system allows for different updating times and rates
	}
	
	
	public void setDesiredSpeedDirection(double desiredSpeed, double desiredAngle){
		this.desiredAngle = desiredAngle;
		this.desiredSpeed = desiredSpeed;
	}
	public void setDesiredTurnSpeed(double turnSpeed){
		this.desiredTurnSpeed=turnSpeed;
	}
}