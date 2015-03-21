package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.CloseGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.ForwardCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.SetWinch;

public class LandfillStack extends Autonomous {
	public LandfillStack() {
		super("LandfillStack", new Step[] {new ForwardCharge(lidar), new CloseGrabber(grabber), new SetWinch(2)});
	}
}
