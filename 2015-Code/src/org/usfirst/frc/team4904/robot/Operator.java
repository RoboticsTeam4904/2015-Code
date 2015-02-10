package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.output.Winch;
import org.usfirst.frc.team4904.robot.output.WinchAction;

public abstract class Operator implements Disablable, Updatable {
	private final Winch winch;
	private final AutoAlign align;
	private volatile int winchTimer;
	public static final int MODE_THIN_TOTE = 0;
	public static final int MODE_WIDE_TOTE = 1;
	public static final int MODE_CAN = 2;
	public static final int MODE_EMPTY = 3;
	
	public Operator(Winch winch, AutoAlign align) {
		this.winch = winch;
		this.align = align;
		winchTimer = 0;
	}
	
	protected void raise(int levels) {
		if (align.isInControl()) {
			winch.setHeight(1);
			winchTimer = 5;
		}
	}
	
	protected void lower(int levels) {
		if (align.isInControl()) {
			winch.setHeight(-1);// TODO Made moving the winch a little more accurate than moving at a certain speed for 5 ticks
			winchTimer = 5;
		}
	}
	
	protected void grab(int mode) {
		if (mode == MODE_THIN_TOTE) {
			align.grabTote();
		} else if (mode == MODE_WIDE_TOTE) {
			align.grabTote();
		} else if (mode == MODE_CAN) {
			align.grabCan();
		}
	}
	
	protected void release() {
		align.release();
	}
	
	protected void adjust(double value) {
		if (align.isInControl()) {
			winch.set(value); // Sets winch motor speed
		}
	}
	
	protected void updateWinch() {
		if (!align.isInControl()) {
			return;
		}
		// TODO use PID loop to move winch
	}

	protected void run(WinchAction action) {
		if (align.isInControl()) {
			action.run(align, winch);
		}
	}

	public void disable() {
		winch.set(0);
	}
}