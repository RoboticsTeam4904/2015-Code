package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class OperatorNachi implements Operator {
	
	boolean holdingTote;
	boolean holdingCan;
	int winchTimer;
	LogitechJoystick stick;
	SpeedController winch;
	AutoAlign align;
	
	OperatorNachi(LogitechJoystick stick, SpeedController winch, AutoAlign align){
		holdingTote = false;
		holdingCan = false;
		
		this.stick = stick;
		this.winch = winch;
		this.align = align;
	}
	
	public void update() {
		if(stick.x1.get()){ // When button 1 is pressed, toggle tote grabbing
			if(!holdingTote) grab(0);
			else release(0);
			holdingTote = !holdingTote;
		}
		if(stick.x2.get()){ // When button 2 is pressed, toggle can grabbing
			if(!holdingCan) grab(2);
			else release(2);
			holdingCan = !holdingCan;
		}
		if(stick.x4.get()) raise(1); // When button 4 is pressed, raise the winch one level
		if(stick.x3.get()) lower(1); // When button 3 is pressed, lower the winch one level
		if(stick.x6.get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if(stick.x5.get()) lower(10); // When button 5 is pressed, lower the winch all the way
		adjust(); // Always adjust by Z axis
		
		if(winchTimer > 0) winchTimer--; // Winch can move over multiple cycles, so set a countdown
		else winch.set(0); // If the countdown is done, stop the winch
	}

	public void raise(int levels) {
		winch.set(1);
		winchTimer = 5;
	}

	public void lower(int levels) {
		winch.set(-1);
		winchTimer = 5;
	}

	public void grab(int mode) {
		if(mode == 0) align.toteGrab();
		else if(mode == 1) align.toteGrab();
		else if(mode == 2) align.canGrab();
	}

	public void release(int mode) {
		if(mode == 0) align.toteRelease();
		else if(mode == 1) align.toteRelease();
		else if(mode == 2) align.canRelease();
	}

	public void adjust() {
		winch.set(stick.getZ());
	}

}
