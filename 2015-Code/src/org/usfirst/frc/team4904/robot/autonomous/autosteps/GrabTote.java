package org.usfirst.frc.team4904.robot.autonomous.autosteps;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.autonomous.Step;

public class GrabTote extends Step {
	private AutoAlign align;
	
	public GrabTote(AutoAlign align) {
		this.align = align;
	}
	
	public boolean run() {
		if (align.isGrabberEmpty()) {
			align.grabTote();
		}
		if (align.isCurrentlyAligning()) {
			return false;
		} else {
			desiredWinchHeight = 1;
		}
		return true;
	}
}
