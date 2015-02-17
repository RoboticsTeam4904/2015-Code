package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.autonomous.Step;

public class OpenGrabber extends Step {
	private final AutoAlign align;
	
	public OpenGrabber(AutoAlign align) {
		this.align = align;
	}
	
	public void init() {}
	
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
