package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.input.IMU;
import org.usfirst.frc.team4904.robot.input.LIDAR;
import org.usfirst.frc.team4904.robot.input.UDAR;
import org.usfirst.frc.team4904.robot.output.Grabber;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import org.usfirst.frc.team4904.robot.output.Winch;

public class AutoAlign implements Updatable {
	private final Mecanum mecanum;
	private final UDAR udar;
	private final IMU imu;
	private final LIDAR lidar;
	private final Grabber grabber;
	private final Winch winch;
	
	private enum State {
		EMPTY, ALIGNING_WITH_WIDE_TOTE, ALIGNING_WITH_THIN_TOTE, ALIGNING_WITH_CAN, HOLDING_CAN, HOLDING_THIN_TOTE, HOLDING_WIDE_TOTE, RELEASING_CAN, RELEASING_THIN_TOTE, RELEASING_WIDE_TOTE
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
	
	public void grabTote(boolean wide) {
		if (currentState != State.EMPTY) {// Don't do anything if grabber isn't empty
			return;
		}
		// TODO set "wide" boolean based on sensor data in case wring button pressed
		currentState = wide ? State.ALIGNING_WITH_WIDE_TOTE : State.ALIGNING_WITH_THIN_TOTE;
	}
	
	public void grabCan() {
		if (currentState != State.EMPTY) {
			return;
		}
		if (currentState == State.EMPTY) {// Don't do anything if grabber isn't empty
			currentState = State.ALIGNING_WITH_CAN; // NOTE: Setting the grabber is NOT done in these functions and is instead done the next time update is called
		}
	}
	
	private void releaseTote(boolean wide) {
		if (lidar.getDists()[90] < 200) { // If there is a tote in front of us, align with it
			currentState = wide ? State.RELEASING_WIDE_TOTE : State.RELEASING_THIN_TOTE;
			return;
		}
		currentState = State.EMPTY;
	}
	
	private void releaseCan() {
		if (lidar.getDists()[90] < 200) { // If there is a tote in front of us, align with it
			currentState = State.RELEASING_CAN;
			return;
		}
		currentState = State.EMPTY;
	}
	
	private void alignWithCanTick(boolean grab) {
		int[] UDARdists = udar.getDists();
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
				mecanum.setDesiredSpeedDirection(1, Math.PI / 2);
			} else {
				mecanum.setDesiredSpeedDirection(0, Math.PI / 2);
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
				i = LIDARLines.length;
			} else if (LIDARLines[i + 2] < 0 && LIDARLines[i] > 0) {
				for (int j = 0; j < 2; j++) { // Switch order of assignment so line is from left to right. My first language is English.
					toteFront[i + j + 2] = LIDARLines[i + 2 + j];
				}
				for (int j = 0; j < 2; j++) {
					toteFront[i + j] = LIDARLines[i + j];
				}
				i = LIDARLines.length;
			}
		}
		double angle = Math.atan2(toteFront[3] - toteFront[1], toteFront[2] - toteFront[0]); // Angle of the tote relative to the X axis (us)
		if (angle < Math.PI / 60) {
			winch.set(0);
			mecanum.setDesiredTurnSpeed(0);
			if (lidar.getDists()[90] > 100) {
				mecanum.setDesiredSpeedDirection(1, Math.PI / 2);
			} else {
				mecanum.setDesiredSpeedDirection(0, Math.PI / 2);
				double width = toteFront[2] - toteFront[0];
				if (Math.abs(width - Grabber.THIN_TOTE_WIDTH) < Math.abs(width - Grabber.WIDE_TOTE_WIDTH)) {
					if (grab) {
						currentState = State.HOLDING_THIN_TOTE;
					} else {
						currentState = State.EMPTY;
					}
				} else {
					if (grab) {
						currentState = State.HOLDING_WIDE_TOTE;
					} else {
						currentState = State.EMPTY;
					}
				}
			}
		} else {
			winch.move(0);
			mecanum.setDesiredSpeedDirection(1, angle);
			if (angle > 0) {
				mecanum.setDesiredTurnSpeed(0.25);
			} else {
				mecanum.setDesiredTurnSpeed(-0.25);
			}
		}
	}
	
	private void doAligningTick(boolean grab) {
		switch (currentState) {
			case ALIGNING_WITH_CAN:
				alignWithCanTick(grab);
				return;
			case ALIGNING_WITH_WIDE_TOTE:
			case ALIGNING_WITH_THIN_TOTE:
				alignWithToteTick(grab); // LIDAR Auto align does not differentiate wide from thin totes
				return;
			default: // Should never reach here
				winch.move(0);
				mecanum.setDesiredSpeedDirection(0, 0);
				mecanum.setDesiredTurnSpeed(0);// Stop the robot, otherwise it would just keep going in the same speed and direction it was when grab was called
				return;
		}
	}
	
	public synchronized void update() {
		grabber.setWidth(getDesiredGrabberState());// This is (on purpose) the only place that grabber.setWidth is ever called (other than in disableMotors())
		if (isCurrentlyAligning()) {
			doAligningTick(true); // Do aligning tick and grab
		} else if (isCurrentlyReleasing()) {
			doAligningTick(false);
		}
	}
	
	public boolean isCurrentlyAligning() {
		switch (currentState) {
			case ALIGNING_WITH_CAN:
			case ALIGNING_WITH_THIN_TOTE:
			case ALIGNING_WITH_WIDE_TOTE:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isCurrentlyReleasing() {
		switch (currentState) {
			case RELEASING_CAN:
			case RELEASING_THIN_TOTE:
			case RELEASING_WIDE_TOTE:
				return true;
			default:
				return false;
		}
	}
	
	private int getDesiredGrabberState() {// What state should the grabber be in
		switch (currentState) {
			case ALIGNING_WITH_CAN:
				return Operator.MODE_CAN + 10;
			case ALIGNING_WITH_THIN_TOTE:
				return Operator.MODE_THIN_TOTE + 10;
			case ALIGNING_WITH_WIDE_TOTE:
				return Operator.MODE_WIDE_TOTE + 10;
			case HOLDING_CAN:
			case RELEASING_CAN:
				return Operator.MODE_CAN;
			case RELEASING_THIN_TOTE:
			case HOLDING_THIN_TOTE:
				return Operator.MODE_THIN_TOTE;
			case HOLDING_WIDE_TOTE:
			case RELEASING_WIDE_TOTE:
				return Operator.MODE_WIDE_TOTE;
			case EMPTY:
				return Operator.MODE_EMPTY;
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
			case HOLDING_WIDE_TOTE:
				releaseTote(true);
				return;
			case HOLDING_THIN_TOTE:
				releaseTote(false);
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
	
	public void forceRelease() {
		currentState = State.EMPTY;
	}

	public boolean isDriverLockedOut() {
		return isCurrentlyAligning() || isCurrentlyReleasing();
	}
}