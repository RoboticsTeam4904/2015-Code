package org.usfirst.frc.team4904.robot;

public abstract class Driver implements IUpdatable{
	
	public Driver(Mecanum mecanumDrive, XboxController xboxController){
		this.mecanumDrive=mecanumDrive;
		this.xboxController=xboxController;
	}
	
	protected final Mecanum mecanumDrive;
	protected final XboxController xboxController;
	
	public abstract void update();
	
	protected void setMovement(double speed, double angle){
		mecanumDrive.setDesiredSpeedDirection(speed, angle);
	}
	
	protected void setTurn(double speed){
		mecanumDrive.setDesiredTurnSpeed(speed);
	}
	
}
