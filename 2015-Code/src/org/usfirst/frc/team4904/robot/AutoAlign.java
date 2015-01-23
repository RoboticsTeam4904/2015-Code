package org.usfirst.frc.team4904.robot;

public class AutoAlign implements IUpdatable{
	
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
	// TODO OVERALL: Make aligning multi-threaded OR in update.
	// Robot should still be ticking while aligning is taking place
	public void toteGrab(boolean wide){
		if(currentState!=State.EMPTY){
			return;
		}
		currentState=wide?State.ALIGNING_WITH_WIDE_TOTE:State.ALIGNING_WITH_THIN_TOTE;
		
		// TODO put alignment code
		// TODO set "wide" boolean based on sensor data in case wring button pressed
	}
	
	public void canGrab(){
		if(currentState!=State.EMPTY){
			return;
		}
		currentState=State.ALIGNING_WITH_CAN;
		// TODO put alignment code
		
	}
	
	private void toteRelease(boolean wide){
		// TODO put alignment code
		currentState=State.EMPTY;
	}
	
	private void canRelease(){
		// TODO put alignment code -- why do we need to align when releasing???
		currentState=State.EMPTY;
	}

	public void update() {
		grabber.setWidth(getDesiredGrabberState());
	}
	public int getDesiredGrabberState(){
		switch(currentState){
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
		}
		//should never get here
		return 5021;
	}
	public void release(){
		switch(currentState){
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
				//You pressed the release button when you aren't holding anything
		}
	}
	
}
