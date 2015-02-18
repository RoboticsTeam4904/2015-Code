package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.input.SuperEncoder;

public class SpeedEncodedMotor extends EncodedMotor {
	private LogKitten logger;
	
	public SpeedEncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel, encoder, pid);
		logger = new LogKitten("SpeedEncodedMotor" + channel, LogKitten.LEVEL_VERBOSE);
	}
	
	public void setValue(double value) {
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
		logger.v("set", "Set value to " + value);
	}
	
	protected double currentState() {
		return encoder.currentEncoderSpeed();
	}
}
