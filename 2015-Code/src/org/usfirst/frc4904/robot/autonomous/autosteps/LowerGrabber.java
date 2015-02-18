package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class LowerGrabber extends Step {
	public LowerGrabber(int stackHeight) {
		desiredWinchHeight = stackHeight;
	}
	
	public void init() {}
	
	public boolean run() {
		return desiredWinchHeight == currentWinchHeight;
	}
}
