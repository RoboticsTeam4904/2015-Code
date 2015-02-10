package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.driver.AutoDriver;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;
import org.usfirst.frc.team4904.robot.output.WinchAction;

public abstract class Autonomous extends Thread implements Disablable {
	private AutoOperator operator = null;
	private AutoDriver driver = null;
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile WinchAction[] winchAction = new WinchAction[0];
	protected volatile boolean running = false;
	
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
	
	public abstract void run();
	
	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}
	
	public void disable() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
		winchAction = new WinchAction[0];
		running = false;
	}

	public WinchAction[] getDesiredWinchActions() {
		WinchAction[] actions = winchAction;
		winchAction = new WinchAction[0];// Reset so things don't happen lots of times
		return actions;
	}
	
	protected void setWinchGrabberAction(WinchAction action) {
		winchAction = new WinchAction[] {action};// Hope that there isn't already one queued up
	}
}
