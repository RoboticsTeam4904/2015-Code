package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.BackwardsCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.CloseGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.OpenGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.SetWinch;
import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;
import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.output.Grabber;

public class OneToteMove extends Autonomous {
	protected OneToteMove(IMU imu, Grabber grabber) {
		super(new Step[] {new CloseGrabber(grabber), new SetWinch(0), new BackwardsCharge(imu), new TimedDrive(0, 0.1, 0.2), new SetWinch(0), new OpenGrabber(grabber)});
	}
}
