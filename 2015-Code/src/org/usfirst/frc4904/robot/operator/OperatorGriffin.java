package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;

public class OperatorGriffin extends Operator {
	private final LogKitten logger;
	
	public OperatorGriffin() {
		super("Griffin");
		logger = new LogKitten();
	}
	
	public synchronized void update() {
		// Grabber toggle
		if (stick.buttons[0].get()) { // When button 1 is pressed, toggle tote grabbing
			if (grabber.getState() == Grabber.GrabberState.OPEN || grabber.getState() == Grabber.GrabberState.OPENING || grabber.getState() == Grabber.GrabberState.DISABLED) {
				logger.v("Griffin Grab");
				grab();
			} else {
				logger.v("Griffin Release");
				release();
			}
		}
		// Grabber override
		if (stick.buttons[11].getRaw()) {
			overrideGrabber(stick.getX());
		} else { // Winch override (by default)
			stopOverrideGrabber(); // Make sure grabber is functioning normally
			if (getWinch() <= Winch.MAX_HEIGHT && getWinch() >= 0) {
				if (getWinch() > Winch.MAX_HEIGHT - 0.5) {
					double x = (getWinch() - (Winch.MAX_HEIGHT - 0.5)) / 0.5;
					overrideWinch(x * (-0.2) + (1 - x) * (stick.getY()));
				} else {
					overrideWinch(stick.getY()); // Always adjust the winch speed via the stick value
				}
			} else if (getWinch() > Winch.MAX_HEIGHT) {
				overrideWinch(-0.2);
			} else if (getWinch() < -0) {
				overrideWinch(0.2);
			}
		}
		// Grabber kill
		if (stick.buttons[9].get()) {
			grabber.disable();
			logger.w("Grabber killed by operator (Griffin)");
		}
		// E-kill
		if (stick.buttons[7].get()) {
			disable();
			logger.w("Robot killed by operator (Griffin)");
		}
	}
	
	public void disable() {
		overrideWinch(0);
	}
}