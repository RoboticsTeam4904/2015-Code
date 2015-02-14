package org.usfirst.frc.team4904.robot.autonomous.autosteps;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.autonomous.Step;

public class StackTote extends Step {
	private final AutoAlign align;
	
	public StackTote(AutoAlign align) {
		this.align = align;
	}
	
	public boolean run() {
		if (align.isGrabberEmpty()) {
			return true;
		} else {
			if (!align.isCurrentlyReleasing()) {
				align.release();
			}
			return false;
		}
	}
}
