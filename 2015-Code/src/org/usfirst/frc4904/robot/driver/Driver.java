package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Named;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.XboxController;
import org.usfirst.frc4904.robot.output.Mecanum;

public abstract class Driver implements Disablable, Updatable, Named {
	private static Mecanum mecanumDrive;
	protected static XboxController xboxController;
	private final String name;
	
	public Driver(String name) {
		this.name = name;
	}
	
	public static void passSensors(Mecanum mecanumDrive, XboxController xboxController) {
		Driver.mecanumDrive = mecanumDrive;
		Driver.xboxController = xboxController;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void update();
	
	protected void setMovement(double x, double y) {
		mecanumDrive.setDesiredXYSpeed(x, y);
	}
	
	protected void setTurn(double speed) {
		mecanumDrive.setDesiredTurnSpeed(speed);
	}
	
	protected void setAngle(double angle) {
		mecanumDrive.setDesiredAngle(angle);
	}
	
	protected void zeroMecanum() {
		mecanumDrive.zero();
	}
	
	public void disable() {
		setMovement(0, 0);
		setTurn(0);
	}
}