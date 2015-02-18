package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.driver.AutoDriver;
import org.usfirst.frc4904.robot.operator.AutoOperator;

public class Autonomous implements Updatable, Disablable {
	private AutoOperator operator = null;
	private AutoDriver driver = null;
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile int desiredWinchHeight = 0;
	protected volatile int currentWinchHeight = 0;
	protected final Step[] steps;
	private boolean finished = false;
	private boolean firstInit = false;
	int currentStep = 0;
	LogKitten logger;
	
	protected Autonomous(Step[] steps) {
		this.steps = steps;
		logger = new LogKitten("Autonomous", LogKitten.LEVEL_DEBUG);
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
		if (finished) {
			disable();
			return;
		}
		if (!firstInit) {
			firstInit = true;
			steps[0].init();
		}
		Step step = steps[currentStep];
		step.setCurrentWinchHeight(currentWinchHeight);
		boolean stepCompleted = step.run();
		double[] movement = step.getDesiredMovement();
		this.desiredTurnSpeed = movement[2];
		this.desiredWinchHeight = step.getDesiredWinchHeight();
		this.desiredXMovement = movement[0];
		this.desiredYMovement = movement[1];
		if (stepCompleted) {
			System.out.println(currentStep + "completed");
			currentStep++;
			if (currentStep >= steps.length) {// Otherwise, this would throw an ArrayIndexOutOfBoundsException when the last step finished
				finished = true;
				disable();
				return;
			}
			steps[currentStep].init();
			disable(); // A step might leave the desiredMovement variables in a nonzero state
		}
	}
	
	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}
	
	public int getDesiredWinchHeight() {
		return desiredWinchHeight;
	}
	
	public void setCurrentWinchHeight(int currentWinchHeight) {
		this.currentWinchHeight = currentWinchHeight;
	}
	
	public void disable() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
		desiredWinchHeight = currentWinchHeight;
		currentStep = 0;
	}
}
