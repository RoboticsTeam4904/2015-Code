package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Overridable;
import org.usfirst.frc4904.robot.Robot;
import org.usfirst.frc4904.robot.TimeSafeguard;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.PDP;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber extends Talon implements Disablable, Updatable, Overridable<Double> {
	public static final int RIGHT_OUTER_SWITCH = 0;
	public static final int LEFT_OUTER_SWITCH = 1;
	public static final int RIGHT_INNER_SWITCH = 2;
	public static final int LEFT_INNER_SWITCH = 3;
	public static final int PDP_PORT = 1;
	private static final double MAX_AMPS = 8; // Tune this value
	private static final double LIMIT_AMPS = 25;
	private static final int NUM_PAST_CURRENTS = (int) (0.25 / Robot.fastUpdatePeriod); // Number of past currents to average
	private final DigitalInput[] limitSwitches;
	private LogKitten logger;
	private boolean override;
	private PDP pdp;
	private final double[] pastAmperage;
	private int currentPosition = 0;
	private final TimeSafeguard timeSafeguard;
	
	public enum GrabberState { // an enum containing grabber states and their values
		OPEN(0), CLOSED_TOTE(-0.2), CLOSED_CAN(0), OPENING(0.18), CLOSING_TOTE(-0.5), DISABLED(0), CLOSING_CAN(-0.25); // grabber state and values
		public final double motorSpeed; // the architecture allowing the enum states to have values
		
		private GrabberState(double speed) {
			motorSpeed = speed;
		}
	}
	private volatile GrabberState grabberState;
	
	public Grabber(int channel, DigitalInput[] limitSwitches, PDP pdp) {
		super(channel);
		logger = new LogKitten(LogKitten.LEVEL_ERROR);
		this.limitSwitches = limitSwitches;
		this.pdp = pdp;
		grabberState = GrabberState.OPEN;
		override = false;
		pastAmperage = new double[NUM_PAST_CURRENTS];
		timeSafeguard = new TimeSafeguard("Grabber open time safeguard", 3);
	}
	
	public void setDesiredGrabberState(GrabberState state) {
		if (state == grabberState) {
			logger.d("Not changing state");
			return;
		}
		switch (state) {
			case OPEN:
				if (grabberState == GrabberState.OPENING) {
					break;
				}
				grabberState = GrabberState.OPENING;
				logger.v("Setting state to opening");
				break;
			case CLOSED_TOTE:
				if (grabberState == GrabberState.CLOSING_TOTE) {
					break;
				}
				grabberState = GrabberState.CLOSING_TOTE;
				logger.v("Setting state to closing tote");
				break;
			case CLOSED_CAN:
				if (grabberState == GrabberState.CLOSING_CAN) {
					break;
				}
				grabberState = GrabberState.CLOSING_CAN;
				logger.v("Setting state to closing can");
			default:
				throw new Error("Invalid or unsupported state passed to setDesiredGrabberState");
		}
	}
	
	public void update() {
		checkLimitSwitches();
		checkPowerUsage();
		if (grabberState == GrabberState.OPENING) {
			if (!timeSafeguard.isSafe()) {
				grabberState = GrabberState.DISABLED;
				logger.f("WARNING - grabber opened for too long - ekilled");
			}
		} else {
			timeSafeguard.reset();
		}
		if (!override) {
			set(grabberState.motorSpeed);
		}
	}
	
	private void checkLimitSwitches() {
		switch (grabberState) {
			case OPENING:
				if (!limitSwitches[RIGHT_OUTER_SWITCH].get()) { // If limit switch has been hit (get() returns opposite - true if not pressed)
					logger.v("Right outer switch");
					grabberState = GrabberState.OPEN; // Don't go too far
				}
				if (!limitSwitches[LEFT_OUTER_SWITCH].get()) {
					logger.v("Left outer switch");
					grabberState = GrabberState.OPEN; // Don't go too far
				}
				return;
			case CLOSING_TOTE:
				if (!limitSwitches[RIGHT_INNER_SWITCH].get()) { // If limit switch has been hit (get() returns opposite - true if not pressed)
					logger.v("Right inner switch");
					grabberState = GrabberState.CLOSED_TOTE; // Don't go too far
				}
				if (!limitSwitches[LEFT_INNER_SWITCH].get()) {
					logger.v("Left inner switch");
					grabberState = GrabberState.CLOSED_TOTE; // Don't go too far
				}
				return;
			case CLOSING_CAN:
				if (!limitSwitches[RIGHT_INNER_SWITCH].get()) { // If limit switch has been hit (get() returns opposite - true if not pressed)
					logger.v("Right inner switch");
					grabberState = GrabberState.CLOSED_TOTE; // Don't go too far
				}
				if (!limitSwitches[LEFT_INNER_SWITCH].get()) {
					logger.v("Left inner switch");
					grabberState = GrabberState.CLOSED_TOTE; // Don't go too far
				}
				return;
			default:
				return;
		}
	}
	
	private double avgCurrent() {
		double currentCurrent = 0;
		for (double current : pastAmperage) {
			currentCurrent += current;
		}
		currentCurrent /= pastAmperage.length;
		return currentCurrent;
	}
	
	private void checkPowerUsage() {
		SmartDashboard.putNumber("Grabber Motor Current", pdp.getCurrent(PDP_PORT));
		SmartDashboard.putNumber("Avg. Grabber Motor Current", avgCurrent());
		pastAmperage[currentPosition++] = pdp.getCurrent(PDP_PORT);
		currentPosition %= pastAmperage.length;
		double currentCurrent = avgCurrent();
		if (currentCurrent > MAX_AMPS && grabberState == GrabberState.CLOSING_TOTE) {
			grabberState = GrabberState.CLOSED_TOTE;
			logger.v("Stopped closing - current hit " + currentCurrent);
		}
		if (currentCurrent > MAX_AMPS * 3.0 / 4.0 && grabberState == GrabberState.CLOSING_CAN) {
			grabberState = GrabberState.CLOSED_CAN;
			logger.v("Stopped closing (CAN) - current hit " + currentCurrent);
		}
		if (currentCurrent > LIMIT_AMPS) {
			grabberState = GrabberState.DISABLED;
			logger.f("WARNING: Too much current: " + currentCurrent + " > " + LIMIT_AMPS);
		}
	}
	
	public void disable() {
		grabberState = GrabberState.DISABLED;
		set(0);
		stopOverride();
	}
	
	public GrabberState getState() {
		return grabberState;
	}
	
	public void override(Double speed) {
		override = true;
		super.set(speed);
	}
	
	public void stopOverride() {
		override = false;
	}
}