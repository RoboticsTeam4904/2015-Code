package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.autonomous.Step;
import org.usfirst.frc.team4904.robot.driver.AutoDriver;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;

public abstract class Autonomous implements Updatable, Disablable {
	private AutoOperator operator = null;
	private AutoDriver driver = null;
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile int desiredWinchHeight = 0;
	protected final Step[] steps;
	int step = 0;
	
	public Autonomous(Step[] steps) {
		this.steps = steps;
	}
	
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

	public void update() {
		boolean increase = steps[step].run();
		this.desiredTurnSpeed = steps[step].desiredTurnSpeed;
		this.desiredWinchHeight = steps[step].desiredWinchHeight;
		this.desiredXMovement = steps[step].desiredXMovement;
		this.desiredYMovement = steps[step].desiredYMovement;
		if (increase) {
			step++;
		}
	}

	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}

	public int getDesiredWinchHeight() {
		return desiredWinchHeight;
	}

	public void disable() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
	}
}
