package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.output.GrabRelease;
import org.usfirst.frc.team4904.robot.output.WinchAction;

public class AutonomousGrab2Totes extends SimpleAutonomous {
	public void update() {
		desiredXMovement = 1;
		desiredYMovement = 1;
		desiredTurnSpeed = 1;
		winchAction = new WinchAction[] {new GrabRelease(true, true)};
	}
}
