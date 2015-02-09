package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.driver.AutoDriver;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;

public abstract class Autonomous implements Updatable, Disablable {
	private AutoOperator operator = null;
	private AutoDriver driver = null;

	public AutoDriver getAutoDriver() {
		return driver;
	}

	public AutoOperator getAutoOperator() {
		return operator;
	}

	public void setAutoOperator(AutoOperator operator) {
		if (this.operator != null) {
			throw new Error("Operator already set");
		}
		this.operator = operator;
	}

	public void setAutoDriver(AutoDriver driver) {
		if (this.driver != null) {
			throw new Error("Driver already set");
		}
		this.driver = driver;
	}

	public abstract double[] getDesiredMovement();
}
