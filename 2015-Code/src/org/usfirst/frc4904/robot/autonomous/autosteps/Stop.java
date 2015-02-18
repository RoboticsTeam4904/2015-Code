package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class Stop extends Step {
	public void init() {}
	
	public boolean run() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
		return false;
	}
}
