package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.input.IMU;

public class BackwardsCharge extends Step {
	private IMU imu;
	
	public BackwardsCharge(IMU imu) {
		desiredXMovement = 0;
		desiredYMovement = -1;
		desiredTurnSpeed = 0;
	}
	
	public void init() {}
	
	public boolean run() {
		return imu.isGoingOverScoringPlatform();
	}
}
