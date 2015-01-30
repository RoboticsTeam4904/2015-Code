package org.usfirst.frc.team4904.robot.driver;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Driver;
import org.usfirst.frc.team4904.robot.input.XboxController;
import org.usfirst.frc.team4904.robot.output.Mecanum;

public class DriverNathan extends Driver {
	private final XboxController xboxController;
	
	public DriverNathan(Mecanum mecanumDrive, XboxController xboxController, AutoAlign align) {
		super(mecanumDrive, align);
		this.xboxController = xboxController;
	}
	
	public synchronized void update() {
		// System.out.println("X: " + xboxController.getX() + " Y: " + xboxController.getY());
		double angle = Math.atan2(-xboxController.getValue(XboxController.X_STICK), -xboxController.getValue(XboxController.Y_STICK));
		double speed = Math.sqrt(xboxController.getValue(XboxController.Y_STICK) * xboxController.getValue(XboxController.Y_STICK) + xboxController.getValue(XboxController.X_STICK) * xboxController.getValue(XboxController.X_STICK)); // TODO Fix because that is not how trig works
		setMovement(speed, angle);
		double turnSpeed = xboxController.getValue(XboxController.TWIST_STICK);
		setTurn(turnSpeed); // Actually do the turning
	}
}