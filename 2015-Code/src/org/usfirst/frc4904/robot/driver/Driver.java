package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Named;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.XboxController;
import org.usfirst.frc4904.robot.output.Mecanum;

public abstract class Driver implements Disablable, Updatable, Named {
	private static Mecanum mecanumDrive;
	private static AutoAlign align;
	protected static XboxController xboxController;
	private final String name;
	
	public Driver(String name) {
		this.name = name;
	}
	
	public static void passSensors(Mecanum mecanumDrive, AutoAlign align, XboxController xboxController) {
		Driver.mecanumDrive = mecanumDrive;
		Driver.align = align;
		Driver.xboxController = xboxController;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void update();
	
	protected void setMovement(double x, double y) { // All movement passes through here so that AutoAlign has precedence
		if (align.isCurrentlyAligning()) {
			mecanumDrive.setDesiredXYSpeed(x, y);
		}
	}
	
	protected void setTurn(double speed) {
		if (align.isCurrentlyAligning()) {
			mecanumDrive.setDesiredTurnSpeed(speed);
		}
	}
	
	protected void autoAlign() {
		align.alignWithTote();
	}
	
	protected void abortAlign() {
		align.abortAlign();
	}
}