package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.BackwardsCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;
import org.usfirst.frc4904.robot.input.IMU;

public class AutoZoneMove extends Autonomous {
	protected AutoZoneMove(IMU imu) {
		super(new Step[] {new BackwardsCharge(imu), new TimedDrive(0, 0.25, 0.5)});
		System.out.println("AUTOZONEMOVE");
	}
}
