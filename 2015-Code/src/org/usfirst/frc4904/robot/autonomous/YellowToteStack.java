// TODO Danger: goes really really fast (like warp 9 or so)
// TODO May kill innocents, please test (with innocents nearby)
// TODO May destroy totes and cans (and cats inside totes)
/* Starting position: In staging zone, facing rightmost tote (from DS perspective), with yellow tote between 
 * Synopsis: makes a stack of all 3 yellow totes by strafing, then move backwards until hitting the scoring platform, then inch forward and release
 */
package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.autonomous.autosteps.GrabTote;
import org.usfirst.frc4904.robot.autonomous.autosteps.Stop;
import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;
import org.usfirst.frc4904.robot.autonomous.autosteps.WaitWithMessage;
import org.usfirst.frc4904.robot.input.Camera;
import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.input.LIDAR;

public class YellowToteStack extends Autonomous {
	public YellowToteStack(Camera camera, AutoAlign align, LIDAR lidar, IMU imu) {
		// super(new Step[] {new GrabTote(align), new LiftToteWithClearance(6), new SidewaysToteFind(lidar), new OpenGrabber(align), new LowerGrabber(6), new GrabTote(align), new LiftToteWithClearance(6), new SidewaysToteFind(lidar), new OpenGrabber(align), new LowerGrabber(6), new GrabTote(align), new BackwardsCharge(imu), new TimedDrive(1, 0, 0.5), new OpenGrabber(align), new Stop()});
		super(new Step[] {new GrabTote(align), new WaitWithMessage(1, "LiftToteWithClearance"), new TimedDrive(1, 0, 1), new WaitWithMessage(1, "OpenGrabber"), new WaitWithMessage(1, "LowerGrabber"), new WaitWithMessage(1, "GrabTote"), new WaitWithMessage(1, "LiftToteWithClearance"), new TimedDrive(1, 0, 1), new WaitWithMessage(1, "OpenGrabber"), new WaitWithMessage(1, "LowerGrabber"), new WaitWithMessage(1, "GrabTote"), new TimedDrive(0, -1, 1), new TimedDrive(1, 0, 0.5), new WaitWithMessage(1, "OpenGrabber"), new Stop()});
	}
}
