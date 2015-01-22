package org.usfirst.frc.team4904.robot;

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

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Joystick;

public class Mecanum {
	private static final double I = 0;
	private static final double D = 0;
	private static final double P = 0;
	SpeedController frontLeftWheel;
	SpeedController frontRightWheel;
	SpeedController backLeftWheel;
	SpeedController backRightWheel;
	
	IMU imu;
	private double desiredSpeed;
	private double desiredAngle;
	private double errorIntegral;
	private double errorDerivative;
	private double lastError;
	
	
	public Mecanum(SpeedController frontLeftWheel, SpeedController frontRightWheel, SpeedController backLeftWheel, SpeedController backRightWheel, IMU imu) {
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
		this.imu = imu;
	}
	
	private void move (double desiredSpeed, double desiredAngle, double turnSpeed) {
		// @param	desiredSpeed	double between 0 and 1 specifying wanted motor speed
		// @param	desiredAngle	double between 0 and 2pi specifying wanted angle in radians
		// @param	turnSpeed		double between 0 and 1 specifying rotational speed
		

		double frontLeft;
		double frontRight;
		double backLeft;
		double backRight;

		frontLeft = Math.sin(desiredAngle + Math.PI / 4) + turnSpeed;
		frontRight = Math.cos(desiredAngle + Math.PI / 4) - turnSpeed;
		backLeft = Math.cos(desiredAngle + Math.PI / 4) + turnSpeed;
		backRight = Math.sin(desiredAngle + Math.PI / 4) - turnSpeed;

		double scaleFactor = Math.max(Math.max(Math.max(Math.abs(frontLeft),Math.abs(frontRight)),Math.abs(backLeft)),Math.abs(backRight));

		frontLeftWheel.set(-desiredSpeed * frontLeft / scaleFactor);
		frontRightWheel.set(desiredSpeed * frontRight / scaleFactor);
		backLeftWheel.set(-desiredSpeed * backLeft / scaleFactor);
		backRightWheel.set(desiredSpeed * backRight / scaleFactor);
	}
	
	private void turn(double speed){
		frontLeftWheel.set(speed);
		frontRightWheel.set(speed);
		backLeftWheel.set(speed);
		backRightWheel.set(speed);
	}
	
	public void update() {
		double error = imu.getAngle() - this.desiredAngle;
		this.errorIntegral += error;
		this.errorDerivative = (error-this.lastError)/0.005;
		
		double turnSpeed = this.P*error + this.I*this.errorIntegral + this.D*this.errorDerivative;
		
		this.move(this.desiredSpeed, this.desiredAngle, turnSpeed);
		this.lastError = error;
	}
	
	
	public void setDesiredSpeedDirection(double desiredSpeed, double desiredAngle){
		this.desiredAngle = desiredAngle;
		this.desiredSpeed = desiredSpeed;
	}
	public void setDesiredTurnSpeed(double turnSpeed){
		desiredAngle+=turnSpeed;
	}
	
}
