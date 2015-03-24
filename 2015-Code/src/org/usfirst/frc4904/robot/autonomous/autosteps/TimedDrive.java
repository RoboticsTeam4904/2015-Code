package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class TimedDrive extends Step {
	private double startingTime;
	private final double duration;
	
	public TimedDrive(double x, double y, double duration) {
		desiredXMovement = x;
		desiredYMovement = y;
		this.duration = duration;
	}
	
	public void init() {
		startingTime = System.currentTimeMillis();
	}
	
	public boolean run() {
		return System.currentTimeMillis() - startingTime > duration * 1000;
	}
}
