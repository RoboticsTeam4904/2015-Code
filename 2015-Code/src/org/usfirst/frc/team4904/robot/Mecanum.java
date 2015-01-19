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
	SpeedController frontLeftWheel;
	SpeedController frontRightWheel;
	SpeedController backLeftWheel;
	SpeedController backRightWheel;
	
	IMU imu;
	
	
	public Mecanum(SpeedController frontLeftWheel, SpeedController frontRightWheel, SpeedController backLeftWheel, SpeedController backRightWheel, IMU imu) {
		this.frontLeftWheel = frontLeftWheel;
		this.frontRightWheel = frontRightWheel;
		this.backLeftWheel = backLeftWheel;
		this.backRightWheel = backRightWheel;
		this.imu = imu;
	}
	
	public double fod (double mainStickDirection) {
		double desiredAngle;
		double imuAngle = imu.getAngle();
		double errorAngle = 10;
		
		if (Math.abs(mainStickDirection - imuAngle) < errorAngle) {
			desiredAngle = imuAngle;
		} else {
			desiredAngle = mainStickDirection - imuAngle;
		}
		
		if (desiredAngle < 0) {
			desiredAngle += 360;
		}
		
		desiredAngle = Math.toRadians(desiredAngle);
		
		return desiredAngle;
	}
	
	public void drive (double desiredSpeed, double desiredAngle, double turnSpeed, boolean fod) {
		// @param	desiredSpeed	double between 0 and 1 specifying wanted motor speed
		// @param	desiredAngle	double between 0 and 2pi specifying wanted angle in radians
		// @param	turnSpeed		double between 0 and 1 specifying rotational speed
		
		if (fod) {
			desiredAngle = fod(desiredAngle);
		} else {
			desiredAngle = Math.toRadians(desiredAngle);
		}

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
	
	public void turn (double speed){
		frontLeftWheel.set(speed);
		frontRightWheel.set(speed);
		backLeftWheel.set(speed);
		backRightWheel.set(speed);
	}
}
