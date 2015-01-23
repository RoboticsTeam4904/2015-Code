package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public abstract class Operator {
	
	LogitechJoystick stick;
	SpeedController winch;
	AutoAlign align;
	int winchTimer;
	static final int MODE_THIN_TOTE = 0;
	static final int MODE_WIDE_TOTE = 1;
	static final int MODE_CAN = 2;
	
	public Operator(LogitechJoystick stick, SpeedController winch, AutoAlign align){
		this.stick = stick;
		this.winch = winch;
		this.align = align;
		winchTimer = 0;
	}
	
	public abstract void update(); // Call once per cycle to update
	
	protected void raise(int levels) {
		winch.set(1);
		winchTimer = 5;
	}

	protected void lower(int levels) {
		winch.set(-1);
		winchTimer = 5;
	}

	protected void grab(int mode) {
		if (mode == MODE_THIN_TOTE) align.toteGrab();
		else if (mode == MODE_WIDE_TOTE) align.toteGrab();
		else if (mode == MODE_CAN) align.canGrab();
	}

	protected void release(int mode) {
		if (mode == MODE_THIN_TOTE) align.toteRelease();
		else if (mode == MODE_WIDE_TOTE) align.toteRelease();
		else if (mode == MODE_CAN) align.canRelease();
	}

	protected void adjust(double value) {
		winch.set(value); // Sets winch motor speed
	}
}








