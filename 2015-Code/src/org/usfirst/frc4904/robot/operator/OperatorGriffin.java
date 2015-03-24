package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;

public class OperatorGriffin extends Operator {
	private final LogKitten logger;
	private boolean untouched = true;
	
	public OperatorGriffin() {
		super("Griffin");
		logger = new LogKitten("OperatorGriffin", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
	}
	
	public synchronized void update() {
		// Method 1
		if (!untouched) {
			if (stick.buttons[0].get()) { // When button 1 is pressed, toggle tote grabbing
				if (grabber.getState() == Grabber.GrabberState.OPEN || grabber.getState() == Grabber.GrabberState.OPENING || grabber.getState() == Grabber.GrabberState.DISABLED) {
					logger.v("Griffin Grab");
					grab();
				} else {
					logger.v("Griffin Release");
					release();
				}
			}
		} else {
			untouched = !stick.buttons[0].getRaw(); // technically only runs once
		}
		// Grabber override
		if (stick.buttons[11].getRaw()) {
			overrideGrabber(stick.getX());
		} else {
			if (getWinch() <= Winch.MAX_HEIGHT && getWinch() >= 0) {
				if (getWinch() > Winch.MAX_HEIGHT - 0.5) {
					double x = (getWinch() - (Winch.MAX_HEIGHT - 0.5)) / 0.5;
					setWinchSpeed(x * (-0.2) + (1 - x) * (stick.getY()));
				} else {
					setWinchSpeed(stick.getY()); // Always adjust the winch speed via the stick value
				}
			} else if (getWinch() > Winch.MAX_HEIGHT) {
				setWinchSpeed(-0.2);
			} else if (getWinch() < -0) {
				setWinchSpeed(0.2);
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
		setWinchSpeed(0);
	}
}