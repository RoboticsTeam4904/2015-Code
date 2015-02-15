package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class LowerTote extends Step {
	int stackHeight;
	
	public LowerTote(int stackHeight) {
		this.stackHeight = stackHeight;
	}
	
	public boolean run() {
		desiredWinchHeight = stackHeight;
		return false;
	}
}
