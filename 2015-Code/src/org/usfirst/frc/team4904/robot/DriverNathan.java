package org.usfirst.frc.team4904.robot;

public class DriverNathan extends Driver {
	
	public DriverNathan(Mecanum mecanumDrive, XboxController xboxController){
		super(mecanumDrive,xboxController);
	}
	
	public synchronized void update() {
		double angle = Math.atan2(xboxController.getY(), xboxController.getX());// TODO atan needs a if statement checking if x is negative
		double speed = angle*Math.asin(xboxController.getY());
		
		setMovement(speed, angle);
		
		double turnSpeed = xboxController.getTwist(); // lols cats are fun
		setTurn(turnSpeed);//Actually do the turning
	}

}
