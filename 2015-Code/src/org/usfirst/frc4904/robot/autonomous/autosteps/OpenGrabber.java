package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Grabber.GrabberState;

public class OpenGrabber extends Step {
	private final Grabber grabber;
	
	public OpenGrabber(Grabber grabber) {
		this.grabber = grabber;
	}
	
	public void init() {}
	
	public boolean run() {
		grabber.setDesiredGrabberState(GrabberState.OPEN);
		if (grabber.getState() == GrabberState.OPEN) {
			return true;
		} else {
			return false;
		}
	}
}
