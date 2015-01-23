package org.usfirst.frc.team4904.robot;

public abstract class Driver {
	
	public Driver(Mecanum mecanumDrive, XboxController xboxController, AutoAlign align){
		this.mecanumDrive=mecanumDrive;
		this.xboxController=xboxController;
		this.align=align;
	}
	
	protected final Mecanum mecanumDrive;
	protected final XboxController xboxController;
	protected final AutoAlign align;
	
	public abstract void update();
	
	protected void setMovement(double speed, double angle){
		if(isInControl()){
			mecanumDrive.setDesiredSpeedDirection(speed, angle);
		}
	}
	
	protected void setTurn(double speed){
		if(isInControl()){
			mecanumDrive.setDesiredTurnSpeed(speed);
		}
	}
	private boolean isInControl(){
		return !align.isCurrentlyAligning();
	}
}
