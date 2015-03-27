package org.usfirst.frc4904.robot;


import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.output.Mecanum;

/**
 * The AutoAlign class aligns the robot with totes using LIDAR.
 * To do its job, it needs to be update()'d and told what to do
 * using alignWithTote() and abortAlign().
 */
public class AutoAlign implements Updatable {
	private final Mecanum mecanum;
	private final LIDAR lidar;
	private final LogKitten logger;
	private static final double ALIGN_DEADZONE_WIDTH = 10; // Deadzone width (in mm) within which you are considered aligned (facing the tote)
	private static final double ALIGN_DEADZONE_DISTANCE = 100; // Deadzone distance (in mm) within which you are considered close enough to the tote
	private static final double ALIGN_CLOSE_ANGLE = Math.PI / 60.0; // When you are aligned within this angle (in radians), switch to "close" mode
	private static final double ALIGN_CLOSE_X_SPEED = 0.2; // Magnitude of X speed (0-1) which "close" align moves at.
	private static final double ALIGN_CLOSE_Y_SPEED = 0.5; // Y speed (0-1) which "close" align moves at.
	private static final double ALIGN_FAR_MAX_X_DISTANCE = 1000; // X distance (in mm) at which "far" align should move at max X speed.
	private static final double ALIGN_FAR_MAX_Y_DISTANCE = 1000; // Y distance (in mm) at which "far" align should move at max Y speed.
	private static final double ALIGN_FAR_MAX_X_SPEED = 0.75; // Magnitude of X speed (0-1) which is the fastest "far" align move at.
	private static final double ALIGN_FAR_MAX_Y_SPEED = 0.75; // Y speed (0-1) which is the fastest "far" align move at.
	private static final double ALIGN_FAR_TURN_SPEED = 0.15; // Magnitude of turn speed (0-1) which "far" align moves at.
	
	private enum State {
		IDLE, ALIGNING
	}
	private volatile State currentState;
	
	public AutoAlign(Mecanum mecanum, LIDAR lidar) {
		this.mecanum = mecanum;
		this.lidar = lidar;
		currentState = State.IDLE;
		logger = new LogKitten(LogKitten.LEVEL_VERBOSE);
	}
	
	/**
	 * Begin aligning with tote.
	 */
	public void alignWithTote() {
		logger.v("Starting tote align");
		currentState = State.ALIGNING;
	}
	
	/**
	 * Prematurely stop aligning.
	 */
	public void abortAlign() {
		logger.v("Aborting align");
		currentState = State.IDLE;
	}
	
	/**
	 * A tick of tote alignment. This should be called repeatedly.
	 * This will look for a tote and then align with it using two modes.
	 * "Far" mode will engage when the alignment is way off and will turn
	 * the robot as well as move in the XY proportional to how off it is.
	 * "Close" mode will not turn and will move in the XY with fixed speeds.
	 */
	private void alignWithToteTick() {
		int[] toteFront = lidar.getLine(); // Get the line that represents the tote (hopefully) from the LIDAR
		double xTargetCenter = ((toteFront[2] + toteFront[0]) / 2.0) - LIDAR.LIDAR_MOUNT_OFFSET; // The center of the target tote is the X coordinate of the midpoint of the line. Shifted to make coordinate system centered around robot and not LIDAR.
		double yTargetCenter = (toteFront[3] + toteFront[1]) / 2.0; // The center of the target tote is the X coordinate of the midpoint of the line.
		double angle = Math.atan2(yTargetCenter, xTargetCenter) - (Math.PI / 2.0); // Angle of the tote relative to the Y axis (straight line forward from the LIDAR)
		logger.v("Current angle: " + Double.toString(angle));
		if (Math.abs(angle) <= ALIGN_CLOSE_ANGLE) { // "Close" alignment mode. Will not turn and move forward and sideways at a slow, fixed rate.
			double xSpeed = 0;
			double ySpeed = 0;
			// Figure out X speed.
			// This will only move right or left if the tote center is outside the deadzone.
			if (xTargetCenter < LIDAR.LIDAR_MOUNT_OFFSET - ALIGN_DEADZONE_WIDTH) { // If tote is to our left and outside the deadzone
				xSpeed = -ALIGN_CLOSE_X_SPEED; // Move left
			} else if (xTargetCenter > LIDAR.LIDAR_MOUNT_OFFSET + ALIGN_DEADZONE_WIDTH) { // Else if tote is to our right and outside the deadzone
				xSpeed = ALIGN_CLOSE_X_SPEED; // Move right
			} else { // Otherwise, we're in the deadzone
				xSpeed = 0; // So don't move
			}
			// Figure out Y speed.
			// This will only move forward if nothing is in front of the LIDAR, not based on the distance to the tote.
			// This is to make sure we don't bump into anything.
			if (lidar.getCorrectedAngleDist(0) > ALIGN_DEADZONE_DISTANCE) { // If nothing is within our deadzone
				ySpeed = ALIGN_CLOSE_Y_SPEED; // Move forward
			} else { // Otherwise
				ySpeed = 0; // Don't move
			}
			// If both speeds are 0, we are done aligning.
			// We still want to continue in the function so we stop driving and turning.
			if (xSpeed == 0 && ySpeed == 0) {
				currentState = State.IDLE;
				logger.v("Done aligning");
			}
			mecanum.setDesiredXYSpeed(xSpeed, ySpeed); // Drive using calculated speeds
			mecanum.setDesiredTurnSpeed(0); // Never turn in "close" mode
		} else { // "Far" alignment mode. Will turn at a constant speed and move forward and sideways at a speed proportional to how off it is.
			double distance = Math.sqrt(Math.pow(xTargetCenter, 2) + Math.pow(yTargetCenter, 2)); // Distance to tote
			double xSpeed = distance * Math.cos(angle) * ALIGN_FAR_MAX_X_SPEED / ALIGN_FAR_MAX_X_DISTANCE; // Calculate X speed and scale to max X distance and speed
			double ySpeed = distance * Math.sin(angle) * ALIGN_FAR_MAX_Y_SPEED / ALIGN_FAR_MAX_Y_DISTANCE; // Calculate Y speed and scale to max Y distance and speed
			// If X or Y speeds are above their max, set them to the max.
			if (xSpeed > ALIGN_FAR_MAX_X_SPEED) {
				xSpeed = ALIGN_FAR_MAX_X_SPEED;
			}
			if (ySpeed > ALIGN_FAR_MAX_Y_SPEED) {
				ySpeed = ALIGN_FAR_MAX_Y_SPEED;
			}
			// Calculate desired turn speed.
			// angle should never be 0 (or close to 0) because then it would be in "close" mode, so no "else if" is used.
			double turnSpeed;
			if (angle < 0) { // If the tote is to our right (angle goes counterclockwise from the Y axis centered on the chassis)
				turnSpeed = ALIGN_FAR_TURN_SPEED; // Turn right
			} else { // Otherwise, if it's to our left
				turnSpeed = -ALIGN_FAR_TURN_SPEED; // Turn left
			}
			mecanum.setDesiredXYSpeed(xSpeed, ySpeed); // Move at desired X and Y speeds
			mecanum.setDesiredTurnSpeed(turnSpeed); // Move at desired turn speed
		}
	}
	
	/**
	 * Do one tick of alignment.
	 * This will call the correct alignment function based on the state.
	 */
	private void doAligningTick() {
		switch (currentState) {
			case ALIGNING:
				alignWithToteTick();
				return;
			case IDLE: // Should never reach here, as update() checks the current state
				return;
			default: // Should never reach here - this means the case is unsupported or null
				mecanum.setDesiredXYSpeed(0, 0);
				mecanum.setDesiredTurnSpeed(0); // Stop the robot, otherwise it would just keep going in the same speed and direction it was when grab was called
				return;
		}
	}
	
	/**
	 * Update the AutoAlign system.
	 * This will do an alignment tick if the system is currently aligning.
	 */
	public synchronized void update() {
		if (isCurrentlyAligning()) {
			doAligningTick();
		}
	}
	
	/**
	 * Check if the system is currently aligning.
	 * @return boolean - true if the system is currently aligning
	 */
	public boolean isCurrentlyAligning() {
		return currentState == State.ALIGNING;
	}
}