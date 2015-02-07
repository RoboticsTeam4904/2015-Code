package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.input.IMU;
import org.usfirst.frc.team4904.robot.input.LIDAR;
import org.usfirst.frc.team4904.robot.input.UDAR;
import org.usfirst.frc.team4904.robot.output.Grabber;
import org.usfirst.frc.team4904.robot.output.Grabber.GrabberState;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import org.usfirst.frc.team4904.robot.output.Winch;

public class AutoAlign implements Updatable {
	private final Mecanum mecanum;
	private final UDAR udar;
	private final IMU imu;
	private final LIDAR lidar;
	private final Grabber grabber;
	private final Winch winch;
	private final int THIN_TOTE_WIDTH = 100;
	private final int WIDE_TOTE_WIDTH = 200;
	
	private enum State {
		EMPTY, ALIGNING_WITH_TOTE, ALIGNING_WITH_CAN, HOLDING_CAN, HOLDING_TOTE, RELEASING_CAN, RELEASING_TOTE
	}
	private volatile State currentState;
	
	public AutoAlign(Mecanum mecanum, UDAR udar, LIDAR lidar, IMU imu, Grabber grabber, Winch winch) {
		this.mecanum = mecanum;
		this.udar = udar;
		this.imu = imu;
		this.grabber = grabber;
		this.lidar = lidar;
		this.winch = winch;
		currentState = State.EMPTY;
	}
	
	public void grabTote() {
		if (currentState != State.EMPTY) {// Don't do anything if grabber isn't empty
			return;
		}
		currentState = State.ALIGNING_WITH_TOTE;
	}
	
	public void grabCan() {
		if (currentState != State.EMPTY) {// Don't do anything if grabber isn't empty
			return;
		}
		currentState = State.ALIGNING_WITH_CAN; // NOTE: Setting the grabber is NOT done in these functions and is instead done the next time update is called
	}
	
	private void releaseTote(boolean wide) {
		if (shouldAlignToteBeforeReleasing()) { // If there is a tote in front of us, align with it
			currentState = State.RELEASING_TOTE;
			return;
		}
		currentState = State.EMPTY;
	}
	
	private void releaseCan() {
		if (shouldAlignCanBeforeReleasing()) { // If there is a can in front of us, align with it
			currentState = State.RELEASING_CAN;
			return;
		}
		currentState = State.EMPTY;
	}
	
	private boolean shouldAlignToteBeforeReleasing() {
		return lidar.getDists()[90] < 200;
	}
	
	private boolean shouldAlignCanBeforeReleasing() {
		return udar.read()[2] < 200;
	}
	
	private void alignWithCanTick(boolean grab) {
		double[] UDARdists = udar.read();
		if (UDARdists[2] > 1000) {
			return;
		}
		if (UDARdists[1] > UDARdists[3]) { // If right sensor detects can, turn right
			mecanum.setDesiredTurnSpeed(0.5);
		} else if (UDARdists[3] > UDARdists[1]) { // Otherwise, turn left
			mecanum.setDesiredTurnSpeed(-0.5);
		} else {
			mecanum.setDesiredTurnSpeed(0);
			if (UDARdists[2] > 100) {
				mecanum.setDesiredXYSpeed(0, 1);
			} else {
				mecanum.setDesiredXYSpeed(0, 0);
				if (grab) {
					currentState = State.HOLDING_CAN;
				} else {
					currentState = State.EMPTY;
				}
			}
		}
	}
	
	private void alignWithToteTick(boolean grab) {
		int[] LIDARLines = lidar.getLines();
		int[] toteFront = new int[4];
		for (int i = 0; i < LIDARLines.length; i += 4) {
			if (LIDARLines[i] < 0 && LIDARLines[i + 2] > 0) {
				for (int j = 0; j < 4; j++) {
					toteFront[i] = LIDARLines[i + j];
				}
				break;
			} else if (LIDARLines[i + 2] < 0 && LIDARLines[i] > 0) {
				for (int j = 0; j < 2; j++) { // Switch order of assignment so line is from left to right. My first language is English.
					toteFront[i + j + 2] = LIDARLines[i + 2 + j];
				}
				for (int j = 0; j < 2; j++) {
					toteFront[i + j] = LIDARLines[i + j];
				}
				break;
			}
		}
		double angle = Math.atan2(toteFront[3] - toteFront[1], toteFront[2] - toteFront[0]); // Angle of the tote relative to the X axis (us)
		if (angle < Math.PI / 60) {
			winch.move(0);
			mecanum.setDesiredTurnSpeed(0);
			if (lidar.getDists()[90] > 100) {
				mecanum.setDesiredXYSpeed(0, 1);
			} else {
				mecanum.setDesiredXYSpeed(0, 0);
				double width = toteFront[2] - toteFront[0];
				if (grab) {
					currentState = State.HOLDING_TOTE;
				} else {
					currentState = State.EMPTY;
				}
			}
		} else {
			winch.move(0);
			double x = Math.cos(angle);
			double y = Math.sin(angle);
			mecanum.setDesiredXYSpeed(x, y);
			if (angle > 0) {
				mecanum.setDesiredTurnSpeed(0.25);
			} else {
				mecanum.setDesiredTurnSpeed(-0.25);
			}
		}
	}
	
	private void doAligningTick(boolean grab) {
		switch (currentState) {// ****************************************************************************************************************
			case ALIGNING_WITH_CAN:
				// alignWithCanTick(grab);
				currentState = State.HOLDING_CAN;
				return;
			case ALIGNING_WITH_TOTE:
				currentState = State.HOLDING_TOTE;
				// alignWithToteTick(grab); // LIDAR Auto align does not differentiate wide from thin totes
				return;
			case RELEASING_CAN:
			case RELEASING_TOTE:
				currentState = State.EMPTY;
				return;
			default: // Should never reach here
				winch.move(0);
				mecanum.setDesiredXYSpeed(0, 0);
				mecanum.setDesiredTurnSpeed(0);// Stop the robot, otherwise it would just keep going in the same speed and direction it was when grab was called
				return;
		}
	}
	
	public synchronized void update() {
		grabber.setDesiredGrabberState(getDesiredGrabberState());// This is (on purpose) the only place that grabber.setWidth is ever called (other than in disableMotors())
		if (isCurrentlyAligning()) {
			doAligningTick(true); // Do aligning tick and grab
		} else if (isCurrentlyReleasing()) {
			doAligningTick(false);
		}
	}
	
	public boolean isCurrentlyAligning() {
		switch (currentState) {
			case ALIGNING_WITH_CAN:
				return true;
			case ALIGNING_WITH_TOTE:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isCurrentlyReleasing() {
		switch (currentState) {
			case RELEASING_CAN:
				return true;
			case RELEASING_TOTE:
				return true;
			default:
				return false;
		}
	}
	
	private GrabberState getDesiredGrabberState() {// What state should the grabber be in
		switch (currentState) {
			case ALIGNING_WITH_CAN:
				return Grabber.GrabberState.OPEN;
			case ALIGNING_WITH_TOTE:
				return Grabber.GrabberState.OPEN;
			case HOLDING_CAN:
				return Grabber.GrabberState.CLOSED;
			case RELEASING_CAN:
				return Grabber.GrabberState.OPEN;
			case HOLDING_TOTE:
				return Grabber.GrabberState.CLOSED;
			case RELEASING_TOTE:
				return Grabber.GrabberState.OPEN;
			case EMPTY:
				return Grabber.GrabberState.OPEN;
			default:
				throw new Error("Current state of AutoAlign does not exist/is null");
		}
	}
	
	public void release() {
		if (isCurrentlyAligning()) {// Canceling alignment, e.g. in case it isn't working
			currentState = State.EMPTY;
			return;
		}
		switch (currentState) {
			case HOLDING_TOTE:
				releaseTote(true);
				return;
			case HOLDING_CAN:
				releaseCan();
				return;
			default:
				System.out.println("User error");
				// You pressed the release button when you aren't holding anything
		}
	}
	
	public boolean isGrabberEmpty() {
		return currentState == State.EMPTY;
	}
	
	public boolean isDriverLockedOut() {
		return isCurrentlyAligning() || isCurrentlyReleasing();
	}
}