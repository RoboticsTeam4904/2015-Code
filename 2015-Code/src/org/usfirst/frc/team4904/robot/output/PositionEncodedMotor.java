package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.input.SuperEncoder;

public class PositionEncodedMotor extends EncodedMotor {
	private boolean directSet;
	
	public PositionEncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel, encoder, pid);
		directSet = false;
	}
	
	public void setValue(double value) {
		// This function takes a motor height
		target = value;
		directSet = false;
	}
	
	public void setSpeed(double value) {
		motorOutput = value;
		directSet = true;
	}
	
	public void update() {
		if (!directSet) {
			motorOutput = pid.calculate(target, currentState());
		}
		super.set(motorOutput);
	}
	
	protected double currentState() {
		return encoder.getTicks();
	}
}
