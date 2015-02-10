package org.usfirst.frc.team4904.robot.autonomous;


public abstract class Step {
	public volatile double desiredXMovement = 0;
	public volatile double desiredYMovement = 0;
	public volatile double desiredTurnSpeed = 0;
	public volatile int desiredWinchHeight = 0;
	
	public abstract boolean run();
}
