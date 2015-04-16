package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.CloseGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.OpenGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;

public class OneToteMove extends Autonomous {
	protected OneToteMove() {
		super("OneToteMove", new Step[] {new CloseGrabber(grabber), new TimedDrive(0, 0.5, 2), new OpenGrabber(grabber)});
	}
}