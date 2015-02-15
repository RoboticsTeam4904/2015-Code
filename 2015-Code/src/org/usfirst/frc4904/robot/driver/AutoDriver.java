package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.Autonomous;
import org.usfirst.frc4904.robot.Driver;
import org.usfirst.frc4904.robot.output.Mecanum;

public class AutoDriver extends Driver {
	private final Autonomous auto;

	public AutoDriver(Mecanum mecanumDrive, AutoAlign align, Autonomous auto) {
		super(mecanumDrive, align);
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
