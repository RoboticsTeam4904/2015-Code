package org.usfirst.frc.team4904.robot.autonomous.autosteps;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.autonomous.Step;

public class StackTote extends Step {
	private AutoAlign align;
	
	public StackTote(AutoAlign align) {
		this.align = align;
	}
	
	public boolean run() {
		desiredXMovement = 1;
		if (align.isGrabberEmpty()) {
			desiredWinchHeight = 0;
			return true;
		} else {
			desiredWinchHeight = 1;
			align.release();
			return false;
		}
	}
}
