package org.usfirst.frc4904.robot;


import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.output.Mecanum;

public class AutoAlign implements Updatable {
	private final Mecanum mecanum;
	private final IMU imu;
	private final LIDAR lidar;
	private final LogKitten logger;
	
	private enum State {
		IDLE, ALIGNING
	}
	private volatile State currentState;
	
	public AutoAlign(Mecanum mecanum, LIDAR lidar, IMU imu) {
		this.mecanum = mecanum;
		this.imu = imu;
		this.lidar = lidar;
		currentState = State.IDLE;
		logger = new LogKitten();
	}
	
	public void alignWithTote() {
		currentState = State.ALIGNING;
	}
	
	public void abortAlign() {
		currentState = State.IDLE;
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
				y = 0.5; // TODO Really fast - tune speed
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
			case ALIGNING:
				alignWithToteTick();
				return;
			case IDLE:
				return;
			default: // Should never reach here
				mecanum.setDesiredXYSpeed(0, 0);
				mecanum.setDesiredTurnSpeed(0); // Stop the robot, otherwise it would just keep going in the same speed and direction it was when grab was called
				return;
		}
	}
	
	public synchronized void update() {
		if (isCurrentlyAligning()) {
			doAligningTick(); // Do aligning tick and grab
		}
	}
	
	public boolean isCurrentlyAligning() {
		return currentState == State.ALIGNING;
	}
}