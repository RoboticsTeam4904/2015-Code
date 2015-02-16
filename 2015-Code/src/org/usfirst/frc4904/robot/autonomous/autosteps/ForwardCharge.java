package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.input.LIDAR;

public class ForwardCharge extends Step {
	private LIDAR lidar;
	
	public ForwardCharge(LIDAR lidar) {
		this.lidar = lidar;
	}
	
	public boolean run() {
		if (lidar.getDists()[0] < 100) {
			return true;
		}
		desiredXMovement = 0;
		desiredYMovement = 1;
		desiredTurnSpeed = 0;
		return false;
	}
}
