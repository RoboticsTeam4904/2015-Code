package org.usfirst.frc4904.robot.autonomous.autosteps;


import org.usfirst.frc4904.robot.autonomous.Step;

public class WaitWithMessage extends Step {
	private final double startingTime;
	public final double duration;
	
	public WaitWithMessage(double duration, String message) {
		startingTime = System.currentTimeMillis();
		this.duration = duration;
		System.out.println(message);
	}
	
	public boolean run() {
		return System.currentTimeMillis() - startingTime > duration * 1000;
	}
}
