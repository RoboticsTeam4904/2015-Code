package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Grabber extends Talon implements Updatable {
	public static final int RIGHT_INNER_SWITCH = 0;
	public static final int LEFT_INNER_SWITCH = 1;
	public static final int RIGHT_OUTER_SWITCH = 2;
	public static final int LEFT_OUTER_SWITCH = 3;
	DigitalInput[] limitSwitches = new DigitalInput[4];
	
	public enum GrabberState {
		OPEN, CLOSED, OPENING, CLOSING, DISABLED
	}
	GrabberState grabberState;
	
	public Grabber(int channel, DigitalInput[] limitSwitches) {
		super(channel);
		this.limitSwitches = limitSwitches;
		grabberState = GrabberState.OPEN;
	}

	public void setDesiredGrabberState(GrabberState state) {
		if (state == grabberState) {
			return;
		}
		switch (state) {
			case OPEN:
				grabberState = GrabberState.OPENING;
				break;
			case CLOSED:
				grabberState = GrabberState.OPENING;
				break;
		}
	}
	
	public void move(double speed) {
		super.set(speed);
	}
	
	public void update() {
		checkLimitSwitches();
		switch (grabberState) {
			case OPEN:
				move(0);// Dont go too far
				return;
			case OPENING:
				move(0.5);
				return;
			case CLOSED:
				move(-0.25);// Dont grab too hard
				return;
			case CLOSING:
				move(-0.5);
				return;
			default:
				move(0);
				return;
		}
	}

	private void checkLimitSwitches() {
		switch (grabberState) {
			case OPENING:
				if (!limitSwitches[RIGHT_OUTER_SWITCH].get()) {
					System.out.println("Right outer switch");
					grabberState = GrabberState.OPEN;// Don't go too far
				}
				if (!limitSwitches[LEFT_OUTER_SWITCH].get()) {
					System.out.println("Left outer switch");
					grabberState = GrabberState.OPEN;// Don't go too far
				}
				return;
			case CLOSING:
				if (!limitSwitches[RIGHT_INNER_SWITCH].get()) {
					System.out.println("Right inner switch");
					grabberState = GrabberState.CLOSED;
				}
				if (!limitSwitches[LEFT_INNER_SWITCH].get()) {
					System.out.println("Left inner switch");
					grabberState = GrabberState.CLOSED;
				}
				return;
			default:
		}
	}

	public void disable() {
		grabberState = GrabberState.DISABLED;
	}
}