package org.usfirst.frc.team4904.robot;

public abstract class Driver {
	
	Mecanum mecanumDrive;
	Xbox xboxController;
	
	public abstract void update();
	
	protected void move(double speed, double angle){
		mecanumDrive.drive(speed, angle);
	}
	
	protected void turn(double speed){
		mecanumDrive.turn(speed);
	}
	
}
