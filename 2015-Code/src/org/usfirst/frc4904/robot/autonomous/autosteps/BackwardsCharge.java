package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.input.IMU;

public class BackwardsCharge extends Step {
	private IMU imu;
	private double startingTime;
	
	public BackwardsCharge(IMU imu) {
		desiredXMovement = 0;
		desiredYMovement = -0.5;
		desiredTurnSpeed = 0;
		this.imu = imu;
	}
	
	public void init() {
		imu.zero();
		startingTime = System.currentTimeMillis();
	}
	
	public boolean run() {
		return imu.isGoingOverScoringPlatform() || System.currentTimeMillis() - startingTime > 5000;
	}
}
