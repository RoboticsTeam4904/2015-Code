package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.LogKitten;
import org.usfirst.frc.team4904.robot.input.SuperEncoder;

public class PositionEncodedMotor extends EncodedMotor {
	private LogKitten logger;
	
	public PositionEncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel, encoder, pid);
		logger = new LogKitten("PositionEncodedMotor" + channel, LogKitten.LEVEL_VERBOSE);
	}
	
	public void set(double value) {
		// This function takes a motor height
		target = value;
		override = false;
		logger.v("setValue", "Set value to " + value);
	}
	
	protected double currentState() {
		return encoder.getTicks();
	}
}
