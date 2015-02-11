// TODO Danger: goes really really fast (like warp 9 or so)
// TODO May kill innocents, please test (with innocents nearby)
// TODO May destroy totes and cans (and cats inside totes)
package org.usfirst.frc.team4904.robot.autonomous;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Autonomous;
import org.usfirst.frc.team4904.robot.autonomous.autosteps.ApproachYellowTote;
import org.usfirst.frc.team4904.robot.autonomous.autosteps.GrabTote;
import org.usfirst.frc.team4904.robot.autonomous.autosteps.Stop;
import org.usfirst.frc.team4904.robot.input.Camera;
import org.usfirst.frc.team4904.robot.input.LIDAR;

public class YellowToteStack extends Autonomous {
	public YellowToteStack(Camera camera, AutoAlign align, LIDAR lidar) {
		super(new Step[] {new ApproachYellowTote(camera, lidar), new GrabTote(align), new Stop()});
	}
}
