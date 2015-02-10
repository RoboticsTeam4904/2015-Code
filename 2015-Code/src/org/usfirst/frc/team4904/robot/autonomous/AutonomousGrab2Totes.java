package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.AutoAlign;

public class AutonomousGrab2Totes extends SimpleAutonomous {
	private final AutoAlign align;
	
	public AutonomousGrab2Totes(AutoAlign align) {
		this.align = align;
	}
	
	public void run() {
		desiredXMovement = 1;
		desiredYMovement = 1;
		desiredTurnSpeed = 1;
		align.grabTote();
	}
}
