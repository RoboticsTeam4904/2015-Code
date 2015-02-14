package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.output.Winch;

public abstract class Operator implements Disablable, Updatable {
	private final Winch winch;
	private final AutoAlign align;
	public static final int MODE_TOTE = 0;
	public static final int MODE_CAN = 1;
	public static final int MODE_EMPTY = 2;
	
	public Operator(Winch winch, AutoAlign align) {
		this.winch = winch;
		this.align = align;
	}
	
	protected void changeHeight(int levels) {
		if (align.isInControl()) {
			winch.setHeight(levels + winch.getHeight());
		}
	}
	
	protected void grab(int mode) {
		if (mode == MODE_TOTE) {
			align.grabTote();
		} else if (mode == MODE_CAN) {
			align.grabCan();
		}
	}
	
	protected void release() {
		align.release();
	}
	
	protected void adjust(double value) {
		// if (align.isInControl()) {
		winch.setSpeed(value); // Sets winch motor speed
		System.out.println("Adjust: " + value);
		// }
	}
	
	public void setWinch(int height) {
		if (align.isInControl()) {
			winch.setHeight(height);
		}
	}
	
	public int getWinch() {
		return winch.getHeight();
	}
	
	public void disable() {
		winch.set(0);
	}
}