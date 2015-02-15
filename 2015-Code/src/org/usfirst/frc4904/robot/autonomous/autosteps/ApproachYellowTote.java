package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;
import org.usfirst.frc4904.robot.input.Camera;
import org.usfirst.frc4904.robot.input.LIDAR;

public class ApproachYellowTote extends Step {
	private Camera camera;
	private LIDAR lidar;
	
	public ApproachYellowTote(Camera camera, LIDAR lidar) {
		this.camera = camera;
		this.lidar = lidar;
	}
	
	public boolean run() {
		desiredYMovement = 1;
		desiredTurnSpeed = 0;
		if (camera.getYellowTote()[0] < 0) {
			desiredXMovement = 0.5;
		} else if (camera.getYellowTote()[0] > 0) {
			desiredXMovement = -0.5;
		} else {
			desiredXMovement = 0;
			desiredYMovement = 0;
		}
		if (lidar.getXY(90)[1] < 100 && lidar.getXY(89)[1] < 100 && lidar.getXY(91)[1] < 100) { // There is probably a better way to do this
			return false;
		} else {
			return true;
		}
	}
}
