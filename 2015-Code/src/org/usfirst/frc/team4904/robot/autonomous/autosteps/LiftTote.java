package org.usfirst.frc.team4904.robot.autonomous.autosteps;


import org.usfirst.frc.team4904.robot.autonomous.Step;

public class LiftTote extends Step {
	public boolean run() {
		desiredWinchHeight = 12;
		return currentWinchHeight == 12;
	}
}
