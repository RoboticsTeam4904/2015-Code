package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.output.GrabRelease;
import org.usfirst.frc.team4904.robot.output.WinchGrabberAction;

public class AutonomousGrab2Totes extends SimpleAutonomous {
	public void run() {
		desiredXMovement = 1;
		desiredYMovement = 1;
		desiredTurnSpeed = 1;
		winchAction = new WinchGrabberAction[] {new GrabRelease(true, true)};
	}
}
