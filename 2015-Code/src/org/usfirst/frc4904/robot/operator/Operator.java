package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Grabber.GrabberState;
import org.usfirst.frc4904.robot.output.Winch;

public abstract class Operator implements Disablable, Updatable {
	protected Winch winch;
	protected AutoAlign align;
	protected Grabber grabber;
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
			winch.setSpeed(-1 * value); // Sets winch motor speed
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
	
	protected void overrideGrabber(double speed) {
		grabber.override(speed / 5);
	}
	
	public void disable() {
		winch.set(0);
	}
}