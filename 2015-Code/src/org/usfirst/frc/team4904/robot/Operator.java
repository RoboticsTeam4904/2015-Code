package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.output.Grabber;
import org.usfirst.frc.team4904.robot.output.Grabber.GrabberState;
import org.usfirst.frc.team4904.robot.output.Winch;

public abstract class Operator implements Disablable, Updatable {
	private final Winch winch;
	private final AutoAlign align;
	private final Grabber grabber;
	public static final int MODE_TOTE = 0;
	public static final int MODE_CAN = 1;
	public static final int MODE_EMPTY = 2;
	
	public Operator(Winch winch, AutoAlign align, Grabber grabber) {
		this.winch = winch;
		this.align = align;
		this.grabber = grabber;
	}
	
	protected void changeHeight(int levels) {
		if (align.isInControl()) {
			winch.setHeight(levels + winch.getHeight());
		}
	}
	
	protected void grab(int mode) {
		grabber.setDesiredGrabberState(GrabberState.OPEN);
		if (mode == MODE_TOTE) {
			align.grabTote();
		} else if (mode == MODE_CAN) {
			align.grabCan();
		}
	}
	
	protected void release() {
		align.release();
	}
	
	protected void adjust(double value) {
		if (align.isInControl()) {
			winch.setSpeed(value); // Sets winch motor speed
		}
	}
	
	protected void setWinch(int height) {
		if (align.isInControl()) {
			winch.setHeight(height);
		}
	}
	
	protected int getWinch() {
		return winch.getHeight();
	}
	
	protected void overrideWinch(double speed) {
		grabber.override(speed / 5);
	}
	
	public void disable() {
		winch.set(0);
	}
}