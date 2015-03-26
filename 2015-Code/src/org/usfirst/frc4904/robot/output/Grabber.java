package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Overridable;
import org.usfirst.frc4904.robot.Robot;
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
	private long openStart;
	private final double[] pastAmperage;
	private int currentPosition = 0;
	
	public enum GrabberState { // an enum containing grabber states and their values
		OPEN(0), CLOSED(-0.1), OPENING(0.5), CLOSING(-0.5), DISABLED(0); // grabber state and values
		public final double motorSpeed; // the architecture allowing the enum states to have values
		
		private GrabberState(double speed) {
			motorSpeed = speed;
		}
	}
	private volatile GrabberState grabberState;
	
	public Grabber(int channel, DigitalInput[] limitSwitches, PDP pdp) {
		super(channel);
		logger = new LogKitten(LogKitten.LEVEL_DEBUG);
		this.limitSwitches = limitSwitches;
		this.pdp = pdp;
		grabberState = GrabberState.OPEN;
		override = false;
		pastAmperage = new double[NUM_PAST_CURRENTS];
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
			case CLOSED:
				if (grabberState == GrabberState.CLOSING) {
					break;
				}
				grabberState = GrabberState.CLOSING;
				logger.v("Setting state to closing");
				break;
			default:
				throw new Error("Invalid or unsupported state passed to setDesiredGrabberState");
		}
	}
	
	public void update() {
		checkLimitSwitches();
		checkPowerUsage();
		if (grabberState == GrabberState.OPENING) {
			if (openStart == 0) {
				openStart = System.currentTimeMillis();
			}
			if (System.currentTimeMillis() - openStart > 10000) {
				openStart = 0;
				grabberState = GrabberState.DISABLED;
				logger.f("WARNING - grabber opened for too long - ekilled");
			}
		} else {
			openStart = 0;
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
				if (!limitSwitches[RIGHT_INNER_SWITCH].get() || !limitSwitches[LEFT_INNER_SWITCH].get()) { // This should fire if the grabber missed the outer switches and rolled around to closing
					logger.f("WARNING: Grabber hit inner limit switches while opening");
					grabberState = GrabberState.DISABLED;
				}
				return;
			case CLOSING:
				if (!limitSwitches[RIGHT_INNER_SWITCH].get()) { // If limit switch has been hit (get() returns opposite - true if not pressed)
					logger.v("Right inner switch");
					grabberState = GrabberState.CLOSED; // Don't go too far
				}
				if (!limitSwitches[LEFT_INNER_SWITCH].get()) {
					logger.v("Left inner switch");
					grabberState = GrabberState.CLOSED; // Don't go too far
				}
				if (!limitSwitches[RIGHT_OUTER_SWITCH].get() || !limitSwitches[LEFT_OUTER_SWITCH].get()) { // This should fire if the grabber missed the inner switches and rolled around to opening
					logger.f("WARNING: Grabber hit outer limit switches while closing");
					grabberState = GrabberState.DISABLED;
				}
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
		logger.d("" + avgCurrent());
		logger.d("" + grabberState);
		pastAmperage[currentPosition++] = pdp.getCurrent(PDP_PORT);
		currentPosition %= pastAmperage.length;
		double currentCurrent = avgCurrent();
		if (currentCurrent > MAX_AMPS) {
			if (grabberState == GrabberState.CLOSING) {
				grabberState = GrabberState.CLOSED;
			}
			logger.v("Stopped closing - current hit " + currentCurrent);
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