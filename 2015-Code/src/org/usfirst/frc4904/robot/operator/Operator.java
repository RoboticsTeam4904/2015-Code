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
	
	public Operator(Winch winch, AutoAlign align, Grabber grabber) {
		this.winch = winch;
		this.align = align;
		this.grabber = grabber;
	}
	
	protected void changeHeight(int levels) {
		winch.setHeight(levels + winch.getHeight());
	}
	
	protected void grab(int mode) {
		grabber.setDesiredGrabberState(GrabberState.CLOSED);
	}
	
	protected void release() {
		grabber.setDesiredGrabberState(GrabberState.OPEN);
	}
	
	protected void setWinchSpeed(double value) {
		winch.overrideSet(-1 * value); // Sets winch motor speed
	}
	
	protected void setWinchHeight(double height) {
		winch.setHeight(height);
	}
	
	protected double getWinch() {
		return winch.getHeight();
	}
	
	protected void overrideGrabber(double speed) {
		grabber.override(speed / 5);
	}
	
	public void disable() {
		winch.set(0);
	}
}