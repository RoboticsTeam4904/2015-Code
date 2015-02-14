package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.input.SuperEncoder;

public class PositionEncodedMotor extends EncodedMotor {
	public PositionEncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel, encoder, pid);
	}
	
	public void setValue(double value) {
		// This function takes a motor height
		target = value;
		override = false;
	}
	
	public void setSpeed(double value) { // Ignore the warning on this line. It is all good anyway.
		motorOutput = value;
		override = true;
	}
	
	public void update() {
		if (!override) {
			motorOutput = pid.calculate(target, currentState());
		}
		super.set(motorOutput);
	}
	
	protected double currentState() {
		return encoder.getTicks();
	}
}
