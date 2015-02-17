package org.usfirst.frc4904.robot.autonomous;


public abstract class Step {
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile int desiredWinchHeight = 0;
	protected volatile int currentWinchHeight = 0;
	
	public abstract boolean run();
	
	public abstract void init();
	
	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}
	
	public int getDesiredWinchHeight() {
		return desiredWinchHeight;
	}
	
	public void setCurrentWinchHeight(int currentWinchHeight) {
		this.currentWinchHeight = currentWinchHeight;
	}
}
