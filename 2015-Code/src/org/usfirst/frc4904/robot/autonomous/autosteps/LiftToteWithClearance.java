package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class LiftToteWithClearance extends Step {
	public LiftToteWithClearance(int stackHeight) {
		desiredWinchHeight = stackHeight;
		desiredXMovement = 0.1;
	}
	
	public boolean run() {
		return currentWinchHeight == desiredWinchHeight;
	}
}