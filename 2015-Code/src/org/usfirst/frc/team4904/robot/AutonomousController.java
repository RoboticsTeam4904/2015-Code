package org.usfirst.frc.team4904.robot;

public class AutonomousController {
	private OperatorAutonomous operator=null;
	private DriverAutonomous driver=null;
	private volatile int desiredWinchAction=0;
	private volatile double angle=0;
	private volatile double speed=0;
	private volatile double turnSpeed=0;
	public AutonomousController(){
		
	}
	public void setDriver(DriverAutonomous driver){
		if(this.driver!=null){
			throw new Error("Driver already set");
		}
		this.driver=driver;
	}
	public void setOperator(OperatorAutonomous operator){
		if(this.operator!=null){
			throw new Error("Operator already set");
		}
		this.operator=operator;
	}
	public void update(){
		// TODO code all autonomous thing here
		desiredWinchAction++;
		angle++;
		speed++;
		turnSpeed++;
	}
	public int getDesiredWinchAction(){
		return desiredWinchAction;
	}
	public double[] getDesiredMovement(){
		return new double[]{angle,speed,turnSpeed};
	}
}
