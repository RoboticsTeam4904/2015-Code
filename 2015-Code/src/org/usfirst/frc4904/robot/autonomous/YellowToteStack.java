// TODO Tune speeds (this goes really fast)
/* Starting position: In staging zone, facing rightmost tote (from DS perspective), with yellow tote between 
 * Synopsis: makes a stack of all 3 yellow totes by strafing, then move backwards until hitting the scoring platform, then inch forward and release
 */
package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.BackwardsCharge;
import org.usfirst.frc4904.robot.autonomous.autosteps.CloseGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.LiftToteWithClearance;
import org.usfirst.frc4904.robot.autonomous.autosteps.OpenGrabber;
import org.usfirst.frc4904.robot.autonomous.autosteps.SetWinch;
import org.usfirst.frc4904.robot.autonomous.autosteps.SidewaysToteFind;
import org.usfirst.frc4904.robot.autonomous.autosteps.TimedDrive;

public class YellowToteStack extends Autonomous {
	public YellowToteStack() {
		super("YellowToteStack", new Step[] {new CloseGrabber(grabber), new LiftToteWithClearance(5), new SidewaysToteFind(lidar), new OpenGrabber(grabber), new SetWinch(0), new CloseGrabber(grabber), new LiftToteWithClearance(5), new SidewaysToteFind(lidar), new OpenGrabber(grabber), new SetWinch(0), new CloseGrabber(grabber), new BackwardsCharge(imu), new TimedDrive(1, 0, 0.5), new OpenGrabber(grabber)});
	}
}
