package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.driver.AutoDriver;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;

public abstract class Autonomous implements Updatable, Disablable {
	private AutoOperator operator;
	private AutoDriver driver;
	
	public Autonomous(AutoDriver driver, AutoOperator operator) {
		this.operator = operator;
		this.driver = driver;
	}
	
	public void update() {}
	
	public void disable() {}
}
