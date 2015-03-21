package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;

public class TimedBackwardsMove extends Autonomous {
	protected TimedBackwardsMove() {
		super(new Step[] {new TimedDrive(0, -0.5, 2)});
	}
}
