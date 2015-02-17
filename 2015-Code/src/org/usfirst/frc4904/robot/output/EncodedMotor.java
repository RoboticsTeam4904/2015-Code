package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.SuperEncoder;
import edu.wpi.first.wpilibj.VictorSP;

public abstract class EncodedMotor extends VictorSP implements Disablable, Updatable {
	protected volatile double target;
	protected PID pid;
	protected double motorOutput;
	protected double P = 1.0; // ticks per second Assuming this is updated at 200Hz, this should result in 0.5 seconds to full speed
	protected double I = 0.3; // ticks
	protected double D = 0.3; // ticks per second per second
	protected boolean override;
	protected final SuperEncoder encoder;
	private LogKitten logger;
	private boolean cap;
	
	public EncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel);
		this.encoder = encoder;
		this.pid = pid;
		motorOutput = 0;
		override = false;
		logger = new LogKitten("EncodedMotor" + channel, LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
		cap = false;
	}
	
	public void trainPID() {
		cap = true;
		pid.train();
	}
	
	public abstract void setValue(double value);
	
	protected abstract double currentState();
	
	public void setSpeed(double value) { // Ignore the warning on this line. It is all good anyway.
		motorOutput = value;
		override = true;
		logger.d("setSpeed", "Set value to " + value);
	}
	
	public void update() {
		if (!override) {
			motorOutput = pid.calculate(target, currentState());
		}
		logger.d("update", "override is: " + Boolean.toString(override) + " motorOutput is: " + Double.toString(motorOutput));
		if (cap && motorOutput > 0.33) {
			motorOutput = 0.33;
		}
		if (cap && motorOutput < -0.33) {
			motorOutput = -0.33;
		}
		super.set(motorOutput);
	}
	
	public void override(double value) {
		super.set(value);
	}
	
	public void disable() {
		super.set(0);
		pid.reset();
		motorOutput = 0;
	}
}
