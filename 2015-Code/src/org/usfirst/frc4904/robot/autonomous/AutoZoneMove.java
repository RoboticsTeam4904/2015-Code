package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.BackwardsCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;
import org.usfirst.frc4904.robot.autonomous.autosteps.WaitWithMessage;

public class AutoZoneMove extends Autonomous {
	protected AutoZoneMove() {
		super("AutoZoneMove", new Step[] {new BackwardsCharge(imu), new WaitWithMessage(2, "Hello"), new TimedDrive(0, 0.5, 1.5)});
	}
}
