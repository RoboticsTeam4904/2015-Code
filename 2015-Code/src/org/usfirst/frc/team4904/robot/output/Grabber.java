package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Grabber extends Talon implements Disablable, Updatable {
	public static final int RIGHT_INNER_SWITCH = 0;
	public static final int LEFT_INNER_SWITCH = 1;
	public static final int RIGHT_OUTER_SWITCH = 2;
	public static final int LEFT_OUTER_SWITCH = 3;
	private final DigitalInput[] limitSwitches;
	
	public enum GrabberState { // an enum containing grabber states and their values
		OPEN(0), CLOSED(-0.25), OPENING(0.5), CLOSING(-0.5), DISABLED(0); // grabber state and values
		public final double motorSpeed; // the architecture allowing the enum states to have values

		private GrabberState(double speed) {
			motorSpeed = speed;
		}
	}
	private volatile GrabberState grabberState;
	
	public Grabber(int channel, DigitalInput[] limitSwitches) {
		super(channel);
		this.limitSwitches = limitSwitches;
		grabberState = GrabberState.OPEN;
	}

	public void setDesiredGrabberState(GrabberState state) {
		if (state == grabberState) {
			// System.out.println("Not changing state");
			return;
		}
		switch (state) {
			case OPEN:
				if (grabberState == GrabberState.OPENING) {
					break;
				}
				grabberState = GrabberState.OPENING;
				System.out.println("Setting state to opening");
				break;
			case CLOSED:
				if (grabberState == GrabberState.CLOSING) {
					break;
				}
				grabberState = GrabberState.CLOSING;
				System.out.println("Setting state to closing");
				break;
			default:
				throw new Error("Invalid or unsupported state passed to setDesiredGrabberState");
		}
	}
	
	public void update() {
		checkLimitSwitches();
		set(grabberState.motorSpeed);
	}

	private void checkLimitSwitches() {
		switch (grabberState) {
			case OPENING:
				if (!limitSwitches[RIGHT_OUTER_SWITCH].get()) { // If limit switch has been hit (get() returns opposite - true if not pressed)
					System.out.println("Right outer switch");
					grabberState = GrabberState.OPEN; // Don't go too far
				}
				if (!limitSwitches[LEFT_OUTER_SWITCH].get()) {
					System.out.println("Left outer switch");
					grabberState = GrabberState.OPEN; // Don't go too far
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
				return;
		}
	}

	public void disable() {
		grabberState = GrabberState.DISABLED;
		set(0);
	}
}