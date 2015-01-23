package org.usfirst.frc.team4904.robot;

public class AutonomousController {
	private OperatorAutonomous operator=null;
	private DriverAutonomous driver=null;
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
		
	}
}
