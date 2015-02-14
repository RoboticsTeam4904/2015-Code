package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.input.SuperEncoder;

public class PositionEncodedMotor extends EncodedMotor {
	public PositionEncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel, encoder, pid);
	}
	
	public void set(double value) {
		// This function takes a motor height
		target = value;
	}
	
	public void setSpeed(double value) {
		super.setSuperSpeed(value);
	}
	
	protected double currentState() {
		return encoder.getTicks();
	}
}
