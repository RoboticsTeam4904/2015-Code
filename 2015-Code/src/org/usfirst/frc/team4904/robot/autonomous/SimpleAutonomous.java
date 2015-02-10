package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.Autonomous;
import org.usfirst.frc.team4904.robot.output.WinchAction;

public abstract class SimpleAutonomous extends Autonomous {
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile WinchAction[] winchAction = new WinchAction[0];
	protected volatile boolean running = false;

	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}

	public void disable() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
		winchAction = new WinchAction[0];
		running = false;
	}
	
	public WinchAction[] getDesiredWinchActions() {
		WinchAction[] actions = winchAction;
		winchAction = new WinchAction[0];// Reset so things don't happen lots of times
		return actions;
	}

	protected void setWinchGrabberAction(WinchAction action) {
		winchAction = new WinchAction[] {action};// Hope that there isn't already one queued up
	}
}
