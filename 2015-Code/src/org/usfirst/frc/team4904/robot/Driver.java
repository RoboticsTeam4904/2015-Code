package org.usfirst.frc.team4904.robot;

public abstract class Driver implements Updatable{
	
	public Driver(Mecanum mecanumDrive, Xbox xboxController){
		this.mecanumDrive=mecanumDrive;
		this.xboxController=xboxController;
	}
	
	Mecanum mecanumDrive;
	Xbox xboxController;
	
	public abstract void update();
	
	protected void move(double speed, double angle){
		mecanumDrive.setDesiredSpeedDirection(speed, angle);
	}
	
	protected void turn(double speed){
		mecanumDrive.setDesiredTurnSpeed(speed);
	}
	
}
