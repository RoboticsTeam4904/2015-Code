package org.usfirst.frc4904.robot;


import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.input.UDAR;
import org.usfirst.frc4904.robot.output.Mecanum;

public class AutoAlign implements Updatable {
	private final Mecanum mecanum;
	private final UDAR udar;
	private final IMU imu;
	private final LIDAR lidar;
	private final LogKitten logger;
	
	private enum State {
		IDLE, ALIGNING_WITH_TOTE, ALIGNING_WITH_CAN
	}
	private volatile State currentState;
	
	public AutoAlign(Mecanum mecanum, UDAR udar, LIDAR lidar, IMU imu) {
		this.mecanum = mecanum;
		this.udar = udar;
		this.imu = imu;
		this.lidar = lidar;
		currentState = State.IDLE;
		logger = new LogKitten("AutoAlign", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
	}
	
	public void alignWithTote() {
		currentState = State.ALIGNING_WITH_TOTE;
	}
	
	public void alignWithCan() {
		currentState = State.ALIGNING_WITH_CAN;
	}
	
	public void abortAlign() {
		currentState = State.IDLE;
	}
	
	private void alignWithCanTick(boolean grab) {
		double[] UDARdists = udar.read();
		if (UDARdists[2] > LIDAR.GRABBER_LENGTH_OFFSET) {
			return;
		}
		if (UDARdists[1] > UDARdists[3]) { // If right sensor detects can, turn right
			mecanum.setDesiredTurnSpeed(0.5);
		} else if (UDARdists[3] > UDARdists[1]) { // Otherwise, turn left
			mecanum.setDesiredTurnSpeed(-0.5);
		} else {
			mecanum.setDesiredTurnSpeed(0);
			if (UDARdists[2] > LIDAR.GRABBER_LENGTH_OFFSET) {
				mecanum.setDesiredXYSpeed(0, 1);
			} else {
				mecanum.setDesiredXYSpeed(0, 0);
				currentState = State.IDLE;
			}
		}
	}
	
	private void alignWithToteTick() {
		int[] toteFront = lidar.getLine();
		double angle = Math.atan2(toteFront[3] - toteFront[1], toteFront[2] - toteFront[0]); // Angle of the tote relative to the X axis (us)
		logger.v("Current angle: " + Double.toString(angle));
		if (Math.abs(angle) < Math.PI / 60) {
			double x = 0;
			double y = 0;
			mecanum.setDesiredTurnSpeed(0);
			if ((toteFront[2] + toteFront[0]) / 2 < LIDAR.LIDAR_MOUNT_OFFSET) {
				x = -0.2;
			} else if ((toteFront[2] + toteFront[0]) / 2 > LIDAR.LIDAR_MOUNT_OFFSET) {
				x = 0.2;
			} else {
				x = 0.0;
			}
			if (lidar.getDists()[0] > 100) {
				y = 0.5; // TODO Really fast (see comment on speeds at top of YellowToteStack)
			} else {
				y = 0;
				currentState = State.IDLE;
			}
			mecanum.setDesiredXYSpeed(x, y);
		} else {
			double x = Math.cos(angle) / 3;
			double y = Math.sin(angle) / 3;
			mecanum.setDesiredXYSpeed(x, y);
			if (angle > 0) {
				mecanum.setDesiredTurnSpeed(0.15);
			} else {
				mecanum.setDesiredTurnSpeed(-0.15);
			}
		}
	}
	
	private void doAligningTick() {
		switch (currentState) {
			case ALIGNING_WITH_CAN:
				currentState = State.ALIGNING_WITH_CAN;
				return;
			case ALIGNING_WITH_TOTE:
				currentState = State.ALIGNING_WITH_TOTE;
				return;
			case IDLE:
				return;
			default: // Should never reach here
				mecanum.setDesiredXYSpeed(0, 0);
				mecanum.setDesiredTurnSpeed(0);// Stop the robot, otherwise it would just keep going in the same speed and direction it was when grab was called
				return;
		}
	}
	
	public synchronized void update() {
		if (isCurrentlyAligning()) {
			doAligningTick(); // Do aligning tick and grab
		}
	}
	
	public boolean isCurrentlyAligning() {
		switch (currentState) {
			case ALIGNING_WITH_CAN:
			case ALIGNING_WITH_TOTE:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isInControl() {
		return !isCurrentlyAligning();
	}
}