package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.AutoAlign;

public class AutonomousGrab2Totes extends Autonomous {
	private final AutoAlign align;

	public AutonomousGrab2Totes(AutoAlign align) {
		super(new Step[] {});
		this.align = align;
	}
}
