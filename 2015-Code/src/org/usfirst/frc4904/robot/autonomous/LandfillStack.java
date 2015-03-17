package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.ForwardCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.GrabTote;
import org.usfirst.frc4904.robot.autonomous.autosteps.LiftTote;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.output.Grabber;

public class LandfillStack extends Autonomous {
	public LandfillStack(LIDAR lidar, Grabber grabber) {
		super(new Step[] {new ForwardCharge(lidar), new GrabTote(grabber), new LiftTote(3)});
	}
}
