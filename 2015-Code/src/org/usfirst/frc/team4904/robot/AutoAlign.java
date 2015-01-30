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
		EMPTY, ALIGNING_WITH_WIDE_TOTE, ALIGNING_WITH_THIN_TOTE, ALIGNING_WITH_CAN, HOLDING_CAN, HOLDING_THIN_TOTE, HOLDING_WIDE_TOTE
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
		// TODO put alignment code
		if (lidar.getDists()[90] < 200) { // If there is a tote in front of us, align with it
			// If we are putting the tote on top of a stack, we need to align before releasing
		}
		// TODO set state to ALIGNING_TO_RELEASE_THIN/WIDE_TOTE so that alignment takes place in doAligningTick
		currentState = State.EMPTY;
	}
	
	private void releaseCan() {
		// TODO put alignment code
		if (lidar.getDists()[90] < 200) { // If there is a tote in front of us, align with it
			// If we are putting the tote on top of a stack, we need to align before releasing
		}
		// TODO set state to ALIGNING_TO_RELEASE_CAN so that alignment takes place in doAligningTick
		currentState = State.EMPTY;
	}
	
	private void alignWithCan() {
		int[] UDARdists = udar.getDists();
		if (UDARdists[2] > 1000) {
			return;
		}
		if (UDARdists[1] > UDARdists[3]) { // If right sensor detects can, turn right
			mecanum.setDesiredTurnSpeed(1);
		} else { // Otherwise, turn left
			mecanum.setDesiredTurnSpeed(-1);
		}
	}
	
	private void alignWithTote() {
		int[] LIDARLines = lidar.getLines();
		// TODO do something with LIDARlines;
		// For now, stop the robot
		winch.move(0);
		mecanum.setDesiredSpeedDirection(0, 0);
		mecanum.setDesiredTurnSpeed(0);// Stop the robot, otherwise it would just keep going in the same speed and direction it was when grab was called
	}
	
	private void doAligningTick() {
		switch (currentState) {
			case ALIGNING_WITH_CAN:
				alignWithCan();
				return;
			case ALIGNING_WITH_THIN_TOTE:
				alignWithTote(); // LIDAR Auto align does not differentiate wide from thin totes
				return;
			case ALIGNING_WITH_WIDE_TOTE:
				alignWithTote();
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
			doAligningTick();
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
	
	private int getDesiredGrabberState() {// What state should the grabber be in
		switch (currentState) {
			case ALIGNING_WITH_CAN:
				return Operator.MODE_CAN + 10;
			case ALIGNING_WITH_THIN_TOTE:
				return Operator.MODE_THIN_TOTE + 10;
			case ALIGNING_WITH_WIDE_TOTE:
				return Operator.MODE_WIDE_TOTE + 10;
			case HOLDING_CAN:
				return Operator.MODE_CAN;
			case HOLDING_THIN_TOTE:
				return Operator.MODE_THIN_TOTE;
			case HOLDING_WIDE_TOTE:
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
				// You pressed the release button when you aren't holding anything
		}
	}
	
	public boolean isGrabberEmpty() {
		return currentState == State.EMPTY;
	}
	
	public void forceRelease() {
		currentState = State.EMPTY;
	}
}