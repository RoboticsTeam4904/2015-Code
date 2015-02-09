package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.Autonomous;

public abstract class SimpleAutonomous extends Autonomous {
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;

	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}
}
