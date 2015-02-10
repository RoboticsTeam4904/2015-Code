// TODO Danger: goes really really fast (like warp 9 or so)
// TODO May kill innocents, please test (with innocents nearby)
// TODO May destroy totes and cans (and cats inside totes)
package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.input.Camera;

public class YellowToteStack extends SimpleAutonomous {
	private int step;
	private Camera camera;
	
	public YellowToteStack(Camera camera) {
		step = 0;
	}
	
	private void step0() {
		desiredYMovement = 1;
		desiredTurnSpeed = 0;
		if (camera.getYellowTote()[0] < 0) {
			desiredXMovement = 0.5;
		} else if (camera.getYellowTote()[0] > 0) {
			desiredXMovement = -0.5;
		} else {
			desiredXMovement = 0;
		}
	}
	
	private void step1() {}
	
	public void update() {
		switch (step) {
			case 0:
				step0();
			case 1:
				step(1);
		}
		desiredXMovement = 1;
		desiredYMovement = 1;
		desiredTurnSpeed = 1;
	}
}
