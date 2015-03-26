package org.usfirst.frc4904.robot;


import edu.wpi.first.wpilibj.Timer;

public class Updater extends Thread { // Function to update automatically in a new thread
	private final Robot robot;
	private final Robot.RobotState robotState;
	private final Updatable[] toUpdate;
	private final double updateSpeed;
	
	public Updater(Robot robot, Robot.RobotState robotState, Updatable[] toUpdate, double updateSpeed) {
		this.robot = robot;
		this.robotState = robotState;
		this.toUpdate = toUpdate;
		this.updateSpeed = updateSpeed;
	}
	
	public static double time() {
		return ((double) System.currentTimeMillis()) / 1000D;
	}
	
	public void run() {
		if (toUpdate.length > 1) {
			for (Updatable u : toUpdate) {
				new Updater(robot, robotState, new Updatable[] {u}, updateSpeed).start();
			}
			return;
		}
		double desiredTime = time() + updateSpeed; // Sync with clock to ensure that update interval is consistent regardless of how long each update takes
		while (robot.getRobotState() == robotState) {
			for (Updatable update : toUpdate) {
				update.update();
			}
			double delay = desiredTime - time();
			if (delay > 0) {
				Timer.delay(delay); // Wait until the time that this tick should end
			}
			desiredTime += updateSpeed; // Next tick should end updatePeriod seconds in the future
		}
	}
}