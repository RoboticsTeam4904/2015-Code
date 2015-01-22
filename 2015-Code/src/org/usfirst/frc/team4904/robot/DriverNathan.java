package org.usfirst.frc.team4904.robot;

public class DriverNathan implements Driver {
	
	Mecanum mecanumDrive;
	Xbox xboxController;
	
	public DriverNathan(Mecanum mecanumDrive, Xbox xboxController){
		this.mecanumDrive = mecanumDrive;
		this.xboxController = xboxController;
	}
	
	public void update() {
		double angle = Math.atan2(xboxController.getY(), xboxController.getX());
		double speed = angle*Math.asin(xboxController.getY());
		
		move(speed, angle);
		
		double turnSpeed = xboxController.getTwist(); // lols cats are fun
	}

	public void move(double speed, double angle) {
		mecanumDrive.drive(speed, angle);
	}

	public void turn(double speed) {
		mecanumDrive.turn(speed);
	}

}
