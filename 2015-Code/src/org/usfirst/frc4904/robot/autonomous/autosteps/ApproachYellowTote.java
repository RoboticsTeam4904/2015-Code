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
	
	public void init() {}
	
	public boolean run() {
		desiredYMovement = 1;
		desiredTurnSpeed = 0;
		desiredXMovement = -1 * camera.getYellowTote()[0];
		return !(lidar.getXY(0)[0] < LIDAR.GRABBER_LENGTH_OFFSET && lidar.getXY(1)[0] < LIDAR.GRABBER_LENGTH_OFFSET && lidar.getXY(359)[0] < LIDAR.GRABBER_LENGTH_OFFSET);
	}
}
