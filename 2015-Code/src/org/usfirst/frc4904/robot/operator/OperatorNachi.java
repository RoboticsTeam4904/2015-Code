package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;

public class OperatorNachi extends Operator {
	private final LogKitten logger;
	private int stackingStep;
	
	public OperatorNachi() {
		super("Nachi");
		logger = new LogKitten();
		stackingStep = 0;
	}
	
	public synchronized void update() {
		// Operator kill on button 7
		if (stick.buttons[6].get()) {
			disable();
			logger.w("Robot killed by operator (Nachi)");
			return;
		}
		// Grabber kill on button 8
		if (stick.buttons[7].get()) {
			grabber.disable();
			logger.w("Grabber killed by operator (Nachi)");
		}
		// When button 11 is pressed, override the winch
		if (stick.buttons[10].getRaw()) {
			overrideWinch(stick.getY());
			System.out.println(stick.getY());
		} else {
			stopOverrideWinch();
		}
		// Grabber override on button 12
		if (stick.buttons[11].getRaw()) {
			overrideGrabber(stick.getY());
		}
		// If we are stacking right now, ignore non-override buttons
		if (stackingStep > 0) {
			if (stick.buttons[0].get()) {
				stackingStep = 0;
			}
			// Stack a tote. This involves a series of synchronous steps:
			// Open the grabber, move down 2 half-totes, close the grabber, move up 2 half-totes.
			logger.v("AutoStacking step " + stackingStep);
			switch (stackingStep) {
				case 1: // Just started - open grabber.
					release();
					stackingStep = 2;
					break;
				case 2: // Wait for grabber to open.
					if (grabber.getState() == Grabber.GrabberState.OPEN || grabber.getState() == Grabber.GrabberState.DISABLED) {
						stackingStep = 3;
					}
					break;
				case 3: // Go down 2 half-totes.
					changeHeight(-2);
					stackingStep = 4;
					break;
				case 4: // Wait for winch to lower.
					if (winch.onTarget()) {
						stackingStep = 5;
					}
					break;
				case 5: // Close grabber.
					grab();
					stackingStep = 6;
					break;
				case 6: // Wait for grabber to close.
					if (grabber.getState() == Grabber.GrabberState.CLOSED) {
						stackingStep = 7;
					}
					break;
				case 7: // Go up 2 half-totes.
					changeHeight(2);
					stackingStep = 8;
					break;
				case 8: // Wait for winch to rise.
					if (winch.onTarget()) {
						stackingStep = 0; // Done stacking.
					}
					break;
				default: // If there's an invalid step
					logger.w("*** invalid stacking step (" + stackingStep + ") - killing stack");
					stackingStep = 0; // Stop stacking
					break;
			}
		} else {
			// When button 1 is pressed, toggle grabbing
			if (stick.buttons[0].get()) {
				if (grabber.getState() == Grabber.GrabberState.OPEN || grabber.getState() == Grabber.GrabberState.DISABLED) {
					logger.v("Nachi Grab");
					grab();
				} else {
					logger.v("Nachi Release");
					release();
				}
			}
			// Winch control buttons. Only one can fire at a time
			// When button 5 is pressed, raise the winch one half-tote
			if (stick.buttons[4].get()) {
				changeHeight(1);
			}
			// When button 3 is pressed, lower the winch one half-tote
			if (stick.buttons[2].get()) {
				changeHeight(-1);
			}
			// When button 6 is pressed, raise the winch all the way
			if (stick.buttons[5].get()) {
				setWinchHeight(Winch.MAX_HEIGHT);
			}
			// When button 4 is pressed, lower the winch all the way
			if (stick.buttons[3].get()) {
				setWinchHeight(0);
			}
			// When button 2 is pressed, stack a tote
			if (stick.buttons[1].get()) {
				stackingStep = 1;
			}
		}
	}
}