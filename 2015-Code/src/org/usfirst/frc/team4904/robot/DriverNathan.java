package org.usfirst.frc.team4904.robot;

public class DriverNathan extends Driver {
	
	public DriverNathan(Mecanum mecanumDrive, XboxController xboxController){
		super(mecanumDrive,xboxController);
	}
	
	public void update() {
		double angle = Math.atan2(xboxController.getY(), xboxController.getX());
		double speed = angle*Math.asin(xboxController.getY());
		
		move(speed, angle);
		
		double turnSpeed = xboxController.getTwist(); // lols cats are fun
		turn(turnSpeed);//Actually do the turning
	}

}
