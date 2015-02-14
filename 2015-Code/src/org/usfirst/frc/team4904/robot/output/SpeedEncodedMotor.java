package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.input.SuperEncoder;

public class SpeedEncodedMotor extends EncodedMotor {
	public SpeedEncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel, encoder, pid);
	}
	
	public void set(double value) {
		// This function takes a motor speed where -1 is -5000 ticks per seond and 1 is 5000 ticks per second
		// ////////////////////////////
		// You do not need to reset the sum when you set the motor speed
		// It is a cumulative variable that is designed to accumulate for continuous pressures on the motor
		// integralSum = 0;
		override = false;
		target = value;
		if (target > 1) {
			target = 1;
		}
		if (target < -1) {
			target = -1;
		}
	}
	
	protected double currentState() {
		return encoder.currentEncoderSpeed();
	}
	
	public void setValue(double value) {
		override = true;
		motorOutput = value;
	}
	
	public void update() {
		if (!override) {
			motorOutput = pid.calculate(target, currentState());
		}
		super.set(motorOutput);
	}
}
