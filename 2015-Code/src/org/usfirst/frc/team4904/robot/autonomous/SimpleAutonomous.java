package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.Autonomous;
import org.usfirst.frc.team4904.robot.output.WinchGrabberAction;

public abstract class SimpleAutonomous extends Autonomous {
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile WinchGrabberAction[] winchAction = new WinchGrabberAction[0];

	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}

	public void disable() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
	}
	
	public WinchGrabberAction[] getDesiredWinchActions() {
		WinchGrabberAction[] actions = winchAction;
		winchAction = new WinchGrabberAction[0];// Reset so things don't happen lots of times
		return actions;
	}

	protected void setWinchGrabberAction(WinchGrabberAction action) {
		winchAction = new WinchGrabberAction[] {action};// Hope that there isn't already one queued up
	}
}
