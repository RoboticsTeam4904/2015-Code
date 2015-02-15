package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.Autonomous;

public class AutonomousGrab2Totes extends Autonomous {
	private final AutoAlign align;

	public AutonomousGrab2Totes(AutoAlign align) {
		super(new Step[] {});
		this.align = align;
	}
}
