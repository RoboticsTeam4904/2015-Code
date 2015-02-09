package org.usfirst.frc.team4904.robot.autonomous;


public class AutonomousType1 extends SimpleAutonomous {
	public void update() {
		desiredXMovement = 1;
		desiredYMovement = 1;
		desiredTurnSpeed = 1;
	}
	
	public void disable() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
	}
}
