package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.output.Mecanum;

public abstract class Driver implements Disablable, Updatable {
	protected final Mecanum mecanumDrive;
	public final AutoAlign align;
	
	public Driver(Mecanum mecanumDrive, AutoAlign align) {
		this.mecanumDrive = mecanumDrive;
		this.align = align;
	}
	
	public abstract void update();
	
	protected void setMovement(double x, double y) {// All movement passes through here so that autoalign has precedence
		if (align.isInControl()) {
			mecanumDrive.setDesiredXYSpeed(x, y);
		}
	}

	protected void setTurn(double speed) {
		if (align.isInControl()) {
			mecanumDrive.setDesiredTurnSpeed(speed);
		}
	}
}