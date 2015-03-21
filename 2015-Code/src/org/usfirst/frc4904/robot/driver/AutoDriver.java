package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.autonomous.Autonomous;

public class AutoDriver extends Driver {
	private final Autonomous auto;
	
	public AutoDriver(Autonomous auto) {
		super("AutoDriver");
		this.auto = auto;
	}
	
	public void disable() {
		setTurn(0);
		setMovement(0, 0);
	}
	
	public void update() {
		double[] movement = auto.getDesiredMovement();
		setMovement(movement[0], movement[1]);
		setTurn(movement[2]);
	}
}
