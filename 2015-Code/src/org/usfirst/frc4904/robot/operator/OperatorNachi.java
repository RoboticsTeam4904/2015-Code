package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.input.LogitechJoystick;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;

public class OperatorNachi extends Operator {
	private final LogitechJoystick stick;
	private final LogKitten logger;
	
	public OperatorNachi(LogitechJoystick stick, Winch winch, AutoAlign align, Grabber grabber) {
		super(winch, align, grabber);
		this.stick = stick;
		this.align = align;
		logger = new LogKitten("OperatorNachi", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
	}
	
	public synchronized void update() {
		if (stick.buttons[10].getRaw()) {
			adjust(stick.getY()); // When button 11 is pressed, adjust the winch
		} else {
			adjust(0);
		}
		if (stick.buttons[0].get()) { // When button 1 is pressed, toggle tote grabbing
			logger.v("update", "TOTE");
			if (grabber.getState() == Grabber.GrabberState.OPEN || grabber.getState() == Grabber.GrabberState.DISABLED) {
				logger.v("update tote", "Grab");
				grab(MODE_TOTE);
			} else {
				logger.v("update tote", "Release");
				release();
			}
		}
		if (stick.buttons[1].get()) { // When button 2 is pressed, toggle can grabbing
			logger.v("update", "CAN");
			if (grabber.getState() == Grabber.GrabberState.OPEN) {
				logger.v("update can", "Grab");
				grab(MODE_CAN);
			} else {
				logger.v("update can", "Release");
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
			setWinch(8); // When button 6 is pressed, raise the winch all the way
		}
		if (stick.buttons[3].get()) {
			setWinch(0); // When button 4 is pressed, lower the winch all the way
		}
		// Grabber override
		if (stick.buttons[11].getRaw()) {
			overrideGrabber(stick.getY());
		}
		// Disable
		if (stick.buttons[7].get()) {
			disable();
			logger.w("update", "disabled");
		}
	}
	
	public void disable() {
		adjust(0);
	}
}