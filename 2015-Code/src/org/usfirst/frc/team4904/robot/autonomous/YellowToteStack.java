// TODO Danger: goes really really fast (like warp 9 or so)
// TODO May kill innocents, please test (with innocents nearby)
// TODO May destroy totes and cans (and cats inside totes)
package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.input.Camera;
import org.usfirst.frc.team4904.robot.output.MoveWinch;
import edu.wpi.first.wpilibj.Timer;

public class YellowToteStack extends SimpleAutonomous {
	private final int step;
	private final Camera camera;
	private final AutoAlign align;

	public YellowToteStack(Camera camera, AutoAlign align) {
		step = 0;
		this.align = align;
		this.camera = camera;
	}

	public void run() {
		while (true) {
			desiredYMovement = 1;
			desiredTurnSpeed = 0;
			if (camera.getYellowTote()[0] < 0) {
				desiredXMovement = 0.5;
			} else if (camera.getYellowTote()[0] > 0) {
				desiredXMovement = -0.5;
			} else {
				desiredXMovement = 0;
				desiredYMovement = 0;
				break;
			}
			Timer.delay(0.1);
		}
		align.grabTote();// Grab the tote
		Timer.delay(0.1);
		while (!align.isCurrentlyAligning()) {// Wait until aligning finishes
			Timer.delay(0.1);
		}
		setWinchGrabberAction(new MoveWinch(1, true));// Move the winch up 1
	}
}
