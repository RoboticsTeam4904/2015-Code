package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.Autonomous;
import org.usfirst.frc4904.robot.autonomous.autosteps.ForwardCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.GrabTote;
import org.usfirst.frc4904.robot.autonomous.autosteps.LiftTote;
import org.usfirst.frc4904.robot.autonomous.autosteps.Stop;
import org.usfirst.frc4904.robot.input.LIDAR;

public class LandfillStack extends Autonomous {
	public LandfillStack(LIDAR lidar, AutoAlign align) {
		super(new Step[] {new ForwardCharge(lidar), new GrabTote(align), new LiftTote(), new Stop()});
	}
}
