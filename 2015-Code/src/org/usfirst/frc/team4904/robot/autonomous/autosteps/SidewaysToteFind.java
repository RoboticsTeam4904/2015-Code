package org.usfirst.frc.team4904.robot.autonomous.autosteps;


import org.usfirst.frc.team4904.robot.autonomous.Step;
import org.usfirst.frc.team4904.robot.input.LIDAR;

public class SidewaysToteFind extends Step {
	private LIDAR lidar;
	private boolean clear;
	
	public SidewaysToteFind(LIDAR lidar) {
		this.lidar = lidar;
		clear = false;
	}
	
	public boolean run() {
		if (!clear) {
			clear = lidar.getDists()[90] > 100;
			return false;
		}
		if (lidar.getDists()[90] < 100) {
			return true;
		}
		return false;
	}
}
