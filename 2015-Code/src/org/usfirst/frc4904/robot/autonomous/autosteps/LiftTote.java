package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class LiftTote extends Step {
	public LiftTote(int stackHeight) {
		desiredWinchHeight = stackHeight;
	}
	
	public void init() {}
	
	public boolean run() {
		return currentWinchHeight == desiredWinchHeight;
	}
}
