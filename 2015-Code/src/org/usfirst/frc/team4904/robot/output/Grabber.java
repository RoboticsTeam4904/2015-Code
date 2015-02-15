package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.LogKitten;
import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.input.PDP;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Grabber extends Talon implements Disablable, Updatable {
	public static final int RIGHT_INNER_SWITCH = 0;
	public static final int LEFT_INNER_SWITCH = 1;
	public static final int RIGHT_OUTER_SWITCH = 2;
	public static final int LEFT_OUTER_SWITCH = 3;
	public static final int PDP_PORT = 1;
	private static final double MAX_AMPS = 4; // Tune this value
	private final DigitalInput[] limitSwitches;
	private LogKitten logger;
	private double overrideSpeed;
	private boolean override;
	private PDP pdp;
	
	public enum GrabberState { // an enum containing grabber states and their values
		OPEN(0), CLOSED(-0.1), OPENING(0.1), CLOSING(-0.25), DISABLED(0); // grabber state and values
		public final double motorSpeed; // the architecture allowing the enum states to have values
		
		private GrabberState(double speed) {
			motorSpeed = speed;
		}
	}
	private volatile GrabberState grabberState;
	
	public Grabber(int channel, DigitalInput[] limitSwitches, PDP pdp) {
		super(channel);
		this.limitSwitches = limitSwitches;
		this.pdp = pdp;
		grabberState = GrabberState.OPEN;
		logger = new LogKitten("Grabber", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
		overrideSpeed = 0;
		override = false;
	}
	
	public void setDesiredGrabberState(GrabberState state) {
		if (state == grabberState) {
			logger.v("setDesiredGrabberState", "Not changing state");
			return;
		}
		switch (state) {
			case OPEN:
				if (grabberState == GrabberState.OPENING) {
					break;
				}
				grabberState = GrabberState.OPENING;
				logger.v("setDesiredGrabberState", "Setting state to opening");
				break;
			case CLOSED:
				if (grabberState == GrabberState.CLOSING) {
					break;
				}
				grabberState = GrabberState.CLOSING;
				logger.v("setDesiredGrabberState", "Setting state to closing");
				break;
			default:
				throw new Error("Invalid or unsupported state passed to setDesiredGrabberState");
		}
	}
	
	public void update() {
		checkLimitSwitches();
		checkPowerUsage();
		if (!override) {
			set(grabberState.motorSpeed);
		} else {
			set(overrideSpeed);
		}
		logger.v("update", "motorSpeed: " + grabberState.motorSpeed);
	}
	
	private void checkLimitSwitches() {
		switch (grabberState) {
			case OPENING:
				if (!limitSwitches[RIGHT_OUTER_SWITCH].get()) { // If limit switch has been hit (get() returns opposite - true if not pressed)
					logger.v("checkLimitSwitches", "Right outer switch");
					grabberState = GrabberState.OPEN; // Don't go too far
				}
				if (!limitSwitches[LEFT_OUTER_SWITCH].get()) {
					logger.v("checkLimitSwitches", "Left outer switch");
					grabberState = GrabberState.OPEN; // Don't go too far
				}
				return;
			case CLOSING:
				if (!limitSwitches[RIGHT_INNER_SWITCH].get()) {
					logger.v("checkLimitSwitches", "Right inner switch");
					grabberState = GrabberState.CLOSED;
				}
				if (!limitSwitches[LEFT_INNER_SWITCH].get()) {
					logger.v("checkLimitSwitches", "Left inner switch");
					grabberState = GrabberState.CLOSED;
				}
				return;
			default:
				return;
		}
	}
	
	private void checkPowerUsage() {
		double currentCurrent = pdp.getCurrent(PDP_PORT);
		if (currentCurrent > MAX_AMPS) {
			switch (grabberState) {
				case OPENING:
					grabberState = GrabberState.OPEN;
					return;
				case CLOSING:
					grabberState = GrabberState.CLOSED;
					return;
				default:
					return;
			}
		}
	}
	
	public void disable() {
		grabberState = GrabberState.DISABLED;
		set(0);
	}
	
	public GrabberState getState() {
		return grabberState;
	}
	
	public void override(double speed) {
		override = true;
		overrideSpeed = speed;
	}
}