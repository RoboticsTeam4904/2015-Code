package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.autonomous.Step;

public class GrabTote extends Step {
	private AutoAlign align;
	
	public GrabTote(AutoAlign align) {
		this.align = align;
	}
	
	public boolean run() {
		if (align.isCurrentlyAligning()) {
			return false;
		}
		if (align.isGrabberEmpty()) {
			align.grabTote();
			return false;
		}
		return true;
	}
}
