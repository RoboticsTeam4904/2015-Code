package org.usfirst.frc.team4904.robot.operator;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.output.Winch;

public class AutoOperator extends Operator {
	public AutoOperator(Winch winch, AutoAlign align) {
		super(winch, align);
	}
	
	public void update() {
		super.updateWinch();
	}
	
	public void raise(int levels) {
		super.raise(levels);
	}
	
	public void lower(int levels) {
		super.lower(levels);
	}
	
	public void grab(int mode) {
		super.grab(mode);
	}
	
	public void adjust(int value) {
		super.adjust(value);
	}
	
	public boolean isGrabberEmpty() {
		return super.isGrabberEmpty();
	}
	
	public void disable() {
		adjust(0);
	}
}
