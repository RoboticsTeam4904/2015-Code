package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.output.Mecanum;

public abstract class Driver implements Updatable {
	public Driver(Mecanum mecanumDrive, AutoAlign align) {
		this.mecanumDrive = mecanumDrive;
		this.align = align;
	}
	protected final Mecanum mecanumDrive;
	private final AutoAlign align;
	
	public abstract void update();
	
	protected void setMovement(double x, double y) {// All movement passes through here so that autoalign has precedence
		if (isInControl()) {
			mecanumDrive.setDesiredXYSpeed(x, y);
		}
	}
	
	protected void setTurn(double speed) {
		if (isInControl()) {
			mecanumDrive.setDesiredTurnSpeed(speed);
		}
	}
	
	private boolean isInControl() {
		return !align.isDriverLockedOut();
	}
}