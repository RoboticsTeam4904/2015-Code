package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Named;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.LogitechJoystick;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Grabber.GrabberState;
import org.usfirst.frc4904.robot.output.Winch;

public abstract class Operator implements Disablable, Updatable, Named {
	protected static LogitechJoystick stick;
	protected static Winch winch;
	protected static Grabber grabber;
	private final String name;
	
	public Operator(String name) {
		this.name = name;
	}
	
	public static void passSensors(LogitechJoystick stick, Winch winch, Grabber grabber) {
		Operator.winch = winch;
		Operator.grabber = grabber;
		Operator.stick = stick;
	}
	
	public String getName() {
		return name;
	}
	
	protected void changeHeight(int levels) {
		winch.changeHeight(levels);
	}
	
	protected void grab() {
		grabber.setDesiredGrabberState(GrabberState.CLOSED);
	}
	
	protected void release() {
		grabber.setDesiredGrabberState(GrabberState.OPEN);
	}
	
	protected void overrideWinch(double speed) {
		winch.override(-1 * speed); // Sets winch motor speed
	}
	
	protected void stopOverrideWinch() {
		winch.stopOverride(); // Reenable winch after override
	}
	
	protected void setWinchHeight(double height) {
		winch.setHeight(height);
	}
	
	protected double getWinch() {
		return winch.getHeight();
	}
	
	protected void overrideGrabber(double speed) {
		grabber.override(speed);
	}
	
	protected void stopOverrideGrabber() {
		grabber.stopOverride();
	}
	
	public void disable() {
		winch.disable();
		grabber.disable();
	}
}