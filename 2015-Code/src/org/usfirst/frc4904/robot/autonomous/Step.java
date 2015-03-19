package org.usfirst.frc4904.robot.autonomous;


public abstract class Step {
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile double desiredWinchHeight = 0;
	protected volatile double currentWinchHeight = 0;
	
	public abstract boolean run();
	
	public abstract void init();
	
	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}
	
	public double getDesiredWinchHeight() {
		return desiredWinchHeight;
	}
	
	public void setCurrentWinchHeight(double currentWinchHeight) {
		this.currentWinchHeight = currentWinchHeight;
	}
}
