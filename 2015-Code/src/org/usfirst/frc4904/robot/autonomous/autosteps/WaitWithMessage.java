package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class WaitWithMessage extends Step {
	private double startingTime;
	public final double duration;
	public final String message;
	
	public WaitWithMessage(double duration, String message) {
		this.duration = duration;
		this.message = message;
	}
	
	public void init() {
		System.out.println(message);
		startingTime = System.currentTimeMillis();
	}
	
	public boolean run() {
		return System.currentTimeMillis() - startingTime > duration * 1000;
	}
}
