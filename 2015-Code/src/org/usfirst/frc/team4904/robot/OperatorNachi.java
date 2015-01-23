package org.usfirst.frc.team4904.robot;


public class OperatorNachi extends Operator {
	
	boolean holdingTote;// TODO these two should be in & handled by autoalign
	boolean holdingCan;
	
	public OperatorNachi(LogitechJoystick stick, Winch winch, AutoAlign align){
		super(stick, winch, align);
		
		holdingTote = false;
		holdingCan = false;
	}
	
	public synchronized void update() {
		if (stick.x1.get()){ // When button 1 is pressed, toggle tote grabbing
			if(!holdingTote) grab(MODE_WIDE_TOTE);
			else release();
			holdingTote = !holdingTote;
		}
		if (stick.x2.get()){ // When button 2 is pressed, toggle can grabbing
			if(!holdingCan) grab(MODE_CAN);
			else release();
			holdingCan = !holdingCan;
		}
		if (stick.x4.get()) raise(1); // When button 4 is pressed, raise the winch one level
		if (stick.x3.get()) lower(1); // When button 3 is pressed, lower the winch one level
		if (stick.x6.get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if (stick.x5.get()) lower(10); // When button 5 is pressed, lower the winch all the way
		adjust(stick.getZ()); // Always adjust by Z axis
		
		updateWinch();
	}

}
