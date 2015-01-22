package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class OperatorGriffin implements Operator {
	
	LogitechJoystick stick;
	SpeedController winch;
	AutoAlign align;
	int winchTimer;
	
	public OperatorGriffin(LogitechJoystick stick, SpeedController winch, AutoAlign align){
		this.stick = stick;
		this.winch = winch;
		this.align = align;
		winchTimer = 0;
	}
	
	public void update() {
		if(stick.x2.get()) adjust(); // When button 2 is pressed, adjust the winch
		if(stick.x4.get()) raise(1); // When button 4 is pressed, raise the winch one level
		if(stick.x3.get()) lower(1); // When button 3 is pressed, lower the winch one level
		if(stick.x6.get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if(stick.x5.get()) lower(10); // When button 5 is pressed, lower the winch all the way
		if(stick.x12.get()) grab(0); // When button 12 is pressed, grab a thin tote
		if(stick.x10.get()) grab(1); // When button 10 is pressed, grab a wide tote
		if(stick.x8.get()) grab(2); // When button 8 is pressed, grab a can
		if(stick.x11.get()) release(0); // When button 11 is pressed, release a thin tote
		if(stick.x9.get()) release(1); // When button 9 is pressed, release a wide tote
		if(stick.x7.get()) release(2); // When button 7 is pressed, release a can
		
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
		winch.set(stick.getY());
	}

}
