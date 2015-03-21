package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.CloseGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.ForwardCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.SetWinch;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.output.Grabber;

public class LandfillStack extends Autonomous {
	public LandfillStack(LIDAR lidar, Grabber grabber) {
		super("LandfillStack", new Step[] {new ForwardCharge(lidar), new CloseGrabber(grabber), new SetWinch(2)});
	}
}
