package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class LiftTote extends Step {
	public LiftTote() {
		desiredWinchHeight = 3;
	}
	
	public boolean run() {
		return currentWinchHeight == desiredWinchHeight;
	}
}
