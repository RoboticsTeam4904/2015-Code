package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Grabber.GrabberState;

public class CloseGrabber extends Step {
	private final Grabber grabber;
	
	public CloseGrabber(Grabber grabber) {
		this.grabber = grabber;
	}
	
	public void init() {
		grabber.setDesiredGrabberState(GrabberState.CLOSED);
	}
	
	public boolean run() {
		System.out.println(grabber.getState());
		if (grabber.getState() == GrabberState.CLOSED) {
			return true;
		} else {
			return false;
		}
	}
}
