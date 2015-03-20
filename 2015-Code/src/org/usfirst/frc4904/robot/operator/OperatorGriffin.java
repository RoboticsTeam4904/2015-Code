package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.input.LogitechJoystick;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;

public class OperatorGriffin extends Operator {
	private final LogitechJoystick stick;
	private final LogKitten logger;
	private final AutoAlign align;
	private boolean untouched = true;
	
	public OperatorGriffin(LogitechJoystick stick, Winch winch, AutoAlign align, Grabber grabber) {
		super(winch, align, grabber);
		this.stick = stick;
		this.align = align;
		logger = new LogKitten("OperatorGriffin", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
	}
	
	public synchronized void update() {
		if (getWinch() < 9.5 && getWinch() > 0) {
			setWinchSpeed(stick.getY()); // Always adjust the winch speed via the stick value
		}
		// Method 1
		if (!untouched) {
			if (stick.buttons[0].get() && (grabber.getState() == Grabber.GrabberState.OPEN || grabber.getState() == Grabber.GrabberState.DISABLED) || grabber.getState() == Grabber.GrabberState.FREEZE) { // if trigger pressed and grabber empty
				logger.v("update", "TOTE Grab");
				grab(MODE_TOTE);
			} else if (!stick.buttons[0].getRaw() && grabber.getState() != Grabber.GrabberState.OPEN) { // if trigger not pressed and grabber not empty
				logger.v("update", "TOTE Release");
				release();
			}
		} else {
			untouched = !stick.buttons[0].getRaw(); // technically only runs once
		}
		if (stick.buttons[1].get()) {
			grabber.setDesiredGrabberState(Grabber.GrabberState.FREEZE);
		}
	}
	
	public void disable() {
		setWinchSpeed(0);
	}
}