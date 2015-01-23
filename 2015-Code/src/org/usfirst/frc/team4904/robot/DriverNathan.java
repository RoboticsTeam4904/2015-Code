package org.usfirst.frc.team4904.robot;

public class DriverNathan extends Driver {
	
	public DriverNathan(Mecanum mecanumDrive, XboxController xboxController){
		super(mecanumDrive,xboxController);
	}
	
	public synchronized void update() {
		double angle = Math.atan2(xboxController.getY(), xboxController.getX());
		double speed = angle * Math.asin(xboxController.getY()); // TODO Fix because that is not how trig works
		
		setMovement(speed, angle);
		
		double turnSpeed = xboxController.getTwist();
		setTurn(turnSpeed); // Actually do the turning
	}
}
