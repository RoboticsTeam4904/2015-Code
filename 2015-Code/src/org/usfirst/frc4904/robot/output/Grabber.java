package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Robot;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.PDP;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber extends Talon implements Disablable, Updatable {
	public static final int RIGHT_OUTER_SWITCH = 0;
	public static final int LEFT_OUTER_SWITCH = 1;
	public static final int INNER_SWITCH = 2;
	public static final int PDP_PORT = 1;
	private static final double MAX_AMPS = 8; // Tune this value
	private static final double LIMIT_AMPS = 15;
	private static final int NUM_PAST_CURRENTS = (int) (0.25 / Robot.fastUpdatePeriod); // Number of past currents to average
	private final DigitalInput[] limitSwitches;
	private LogKitten logger;
	private double overrideSpeed;
	private boolean override;
	private PDP pdp;
	private int negate = 1;
	private long openStart;
	private final double[] pastAmperage;
	private int currentPosition = 0;
	
	public enum GrabberState { // an enum containing grabber states and their values
		OPEN(0), CLOSED(-0.0), OPENING(0.5), CLOSING(-0.5), DISABLED(0); // grabber state and values
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
		logger = new LogKitten("Grabber", LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_ERROR);
		overrideSpeed = 0;
		override = false;
		pastAmperage = new double[NUM_PAST_CURRENTS];
	}
	
	public void negateGrabber() {
		negate *= -1;
	}
	
	public void setDesiredGrabberState(GrabberState state) {
		if (state == grabberState) {
			logger.d("setDesiredGrabberState", "Not changing state");
			return;
		}
		switch (state) {
			case OPEN:
				if (grabberState == GrabberState.OPENING) {
					break;
				}
				grabberState = GrabberState.OPENING;
				logger.w("setDesiredGrabberState", "Setting state to opening");
				break;
			case CLOSED:
				if (grabberState == GrabberState.CLOSING) {
					break;
				}
				grabberState = GrabberState.CLOSING;
				logger.w("setDesiredGrabberState", "Setting state to closing");
				break;
			default:
				throw new Error("Invalid or unsupported state passed to setDesiredGrabberState");
		}
	}
	
	public void update() {
		SmartDashboard.putNumber("Grabber Motor Current", pdp.getCurrent(PDP_PORT));
		SmartDashboard.putNumber("Avg. Grabber Motor Current", avgCurrent());
		System.out.println(avgCurrent() + " | " + pdp.getCurrent(PDP_PORT) + " | " + grabberState);
		pastAmperage[currentPosition++] = pdp.getCurrent(PDP_PORT);
		currentPosition %= pastAmperage.length;
		checkLimitSwitches();
		checkPowerUsage();
		if (grabberState == GrabberState.OPENING && openStart == 0) {
			openStart = System.currentTimeMillis();
		}
		if (openStart > 10000) {
			openStart = 0;
			grabberState = GrabberState.DISABLED;
		}
		if (!override) {
			set(grabberState.motorSpeed * negate);
		} else {
			set(overrideSpeed);
		}
		logger.d("update", "motorSpeed: " + grabberState.motorSpeed * negate);
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
				if (!limitSwitches[INNER_SWITCH].get()) {
					logger.v("checkLimitSwitches", "Inner switch");
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
		double currentCurrent = avgCurrent();
		if (currentCurrent > MAX_AMPS) {
			if (grabberState == GrabberState.CLOSING) {
				grabberState = GrabberState.CLOSED;
			}
			// if (this.get() < 0) negate = 1;
			// else if (this.get() > 0) negate = -1;
			logger.f("checkPowerUsage", "stopped close " + currentCurrent);
		}
		if (currentCurrent > LIMIT_AMPS) {
			grabberState = GrabberState.DISABLED;
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