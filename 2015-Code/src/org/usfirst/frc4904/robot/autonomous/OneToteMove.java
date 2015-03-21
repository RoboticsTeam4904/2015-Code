package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.BackwardsCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.CloseGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.OpenGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.SetWinch;
import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;

public class OneToteMove extends Autonomous {
	protected OneToteMove() {
		super("OneToteMove", new Step[] {new CloseGrabber(grabber), new SetWinch(1), new BackwardsCharge(imu), new TimedDrive(0, 0.1, 0.2), new SetWinch(0), new OpenGrabber(grabber)});
	}
}
