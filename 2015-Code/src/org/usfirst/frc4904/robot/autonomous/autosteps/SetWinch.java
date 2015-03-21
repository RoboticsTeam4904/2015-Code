package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class SetWinch extends Step {
	public SetWinch(int stackHeight) {
		desiredWinchHeight = stackHeight;
	}
	
	public void init() {}
	
	public boolean run() {
		return Math.abs(desiredWinchHeight - currentWinchHeight) < 0.1;
	}
}
