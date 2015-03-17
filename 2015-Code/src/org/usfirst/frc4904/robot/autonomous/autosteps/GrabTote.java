package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Grabber.GrabberState;

public class GrabTote extends Step {
	private final Grabber grabber;
	
	public GrabTote(Grabber grabber) {
		this.grabber = grabber;
	}
	
	public void init() {}
	
	public boolean run() {
		grabber.setDesiredGrabberState(GrabberState.CLOSED);
		if (grabber.getState() == GrabberState.CLOSED) {
			return true;
		} else {
			return false;
		}
	}
}
