package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;

public class OperatorNachi extends Operator {
	private final LogKitten logger;
	
	public OperatorNachi() {
		super("Nachi");
		logger = new LogKitten();
	}
	
	public synchronized void update() {
		if (stick.buttons[10].getRaw()) {
			overrideWinch(stick.getY()); // When button 11 is pressed, adjust the winch
		} else {
			stopOverrideWinch();
		}
		if (stick.buttons[0].get()) { // When button 1 is pressed, toggle tote grabbing
			if (grabber.getState() == Grabber.GrabberState.OPEN || grabber.getState() == Grabber.GrabberState.DISABLED) {
				logger.v("Nachi Grab");
				grab();
			} else {
				logger.v("Nachi Release");
				release();
			}
		}
		if (stick.buttons[4].get()) {
			changeHeight(1); // When button 5 is pressed, raise the winch one level
		}
		if (stick.buttons[2].get()) {
			changeHeight(-1); // When button 3 is pressed, lower the winch one level
		}
		if (stick.buttons[5].get()) {
			setWinchHeight(Winch.MAX_HEIGHT); // When button 6 is pressed, raise the winch all the way
		}
		if (stick.buttons[3].get()) {
			setWinchHeight(0); // When button 4 is pressed, lower the winch all the way
		}
		// Grabber override
		if (stick.buttons[11].getRaw()) {
			overrideGrabber(stick.getY());
		}
		// E-kill
		if (stick.buttons[7].get()) {
			disable();
			logger.w("Robot killed by operator (Nachi)");
		}
	}
	
	public void disable() {
		overrideWinch(0);
	}
}