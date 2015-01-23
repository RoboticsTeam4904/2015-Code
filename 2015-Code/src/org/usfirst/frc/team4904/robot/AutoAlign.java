package org.usfirst.frc.team4904.robot;

public class AutoAlign {
	private final Mecanum mecanum;
	private final UDAR udar;
	private final IMU imu;
	private final LIDAR lidar;
	private final Grabber grabber;
	private enum State{
		EMPTY, ALIGNING_WITH_WIDE_TOTE, ALIGNING_WITH_THIN_TOTE, ALIGNING_WITH_CAN, HOLDING_CAN, HOLDING_THIN_TOTE, HOLDING_WIDE_TOTE
	}
	private volatile State currentState;
	public AutoAlign(Mecanum mecanum, UDAR udar, LIDAR lidar, IMU imu, Grabber grabber){
		this.mecanum = mecanum;
		this.udar = udar;
		this.imu = imu;
		this.grabber = grabber;
		this.lidar=lidar;
	}
	// TODO OVERALL: Make aligning take place in update.
	// Robot should still be ticking while aligning is taking place
	// If the aligning happens in the toteGrab function, then that blocks the ticking of the robot
	public void toteGrab (boolean wide) {
		if (currentState != State.EMPTY) {
			return;
		}
		// TODO set "wide" boolean based on sensor data in case wring button pressed
		currentState = wide ? State.ALIGNING_WITH_WIDE_TOTE : State.ALIGNING_WITH_THIN_TOTE;
	}
	
	public void canGrab(){
		if (currentState !=State.EMPTY) {
			return;
		}
		currentState = State.ALIGNING_WITH_CAN; // NOTE: Setting the grabber is NOT done in these functions and is instead done the next time update is called
	}
	
	private void toteRelease (boolean wide) {
		// TODO put alignment code
		// If we are putting the tote on top of a stack, we need to align before releasing
		currentState = State.EMPTY;
	}
	
	private void canRelease() {
		// TODO put alignment code
		// If we are putting the can on top of a stack, we need to align before releasing
		currentState = State.EMPTY;
	}
	private void doAligningTick() {
		// TODO put alignment code
	}
	public void update() {
		grabber.setWidth(getDesiredGrabberState());
		if (isCurrentlyAligning()) {
			doAligningTick();
		}
	}
	private boolean isCurrentlyAligning () {
		switch (currentState) {
		case ALIGNING_WITH_CAN:
		case ALIGNING_WITH_THIN_TOTE:
		case ALIGNING_WITH_WIDE_TOTE:
			return true;
		default:
			return false;
		}
	}
	private int getDesiredGrabberState () {
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
			return 5;
		default:
			throw new Error("Current state of AutoAlign does not exist");
		}
	}
	public void release () {
		switch (currentState) {
		case HOLDING_WIDE_TOTE:
			toteRelease(true);
			return;
		case HOLDING_THIN_TOTE:
			toteRelease(false);
			return;
		case HOLDING_CAN:
			canRelease();
			return;
		default:
			// You pressed the release button when you aren't holding anything
		}
	}	
}