package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.input.LIDAR;

public class SidewaysToteFind extends Step {
	private LIDAR lidar;
	private boolean clear;
	
	public SidewaysToteFind(LIDAR lidar) {
		this.lidar = lidar;
		clear = false;
	}
	
	public void init() {}
	
	public boolean run() {
		desiredXMovement = 1;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
		if (!clear) {
			clear = lidar.getCorrectedAngleDist(0) > 100;
			return false;
		}
		if (lidar.getCorrectedAngleDist(0) < 100) {
			return true;
		}
		return false;
	}
}
