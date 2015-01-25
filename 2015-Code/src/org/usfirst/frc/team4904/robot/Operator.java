package org.usfirst.frc.team4904.robot;

import org.usfirst.frc.team4904.robot.output.Winch;

public abstract class Operator implements Updatable{
	
	
	private final Winch winch;
	private final AutoAlign align;
	private volatile int winchTimer;
	public static final int MODE_THIN_TOTE = 0;
	public static final int MODE_WIDE_TOTE = 1;
	public static final int MODE_CAN = 2;
	public static final int MODE_EMPTY = 3;
	
	public Operator(Winch winch, AutoAlign align){
		this.winch = winch;
		this.align = align;
		winchTimer = 0;
	}
	
	public abstract void update();
	
	protected void raise(int levels) {
		winch.set(1);
		winchTimer = 5;
	}

	protected void lower(int levels) {
		winch.set(-1);// TODO Mave moving the winch a little more accurate than moving at a certian speed for 5 ticks
		winchTimer = 5;
	}

	protected void grab(int mode) {
		if (mode == MODE_THIN_TOTE) align.grabTote(false);
		else if (mode == MODE_WIDE_TOTE) align.grabTote(true);
		else if (mode == MODE_CAN) align.grabCan();
	}

	protected void release() {
		align.release();
	}

	protected void adjust(double value) {
		winch.set(value); // Sets winch motor speed
	}
	protected void updateWinch(){
		if (winchTimer > 0) winchTimer--; // Winch can move over multiple cycles, so set a countdown
		else
		{
			if(winchTimer==0){
			winch.set(0); // If the countdown is done, stop the winch
			winchTimer=-1; //But only once
			}
		
		
		}
	}
	protected boolean isGrabberEmpty(){//Wrapper function -- references to align and winch are private not protected so subclasses cant access them
		return align.isGrabberEmpty();
	}
}