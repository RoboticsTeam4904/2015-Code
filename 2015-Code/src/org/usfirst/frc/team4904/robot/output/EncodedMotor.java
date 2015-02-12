package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.Robot;
import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.input.SuperEncoder;
import edu.wpi.first.wpilibj.VictorSP;

public abstract class EncodedMotor extends VictorSP implements Disablable, Updatable {
	protected volatile double target;
	private volatile double previousError;
	private volatile double integralSum;
	private volatile double motorOutput;
	protected double P = 1.0; // ticks per second Assuming this is updated at 200Hz, this should result in 0.5 seconds to full speed
	protected double I = 0.3; // ticks
	protected double D = 0.3; // ticks per second per second
	protected final SuperEncoder encoder;
	
	public EncodedMotor(int channel, SuperEncoder encoder) {
		super(channel);
		this.encoder = encoder;
		previousError = 0;
		integralSum = 0;
		motorOutput = 0;
	}
	
	public abstract void set(double value);
	
	protected abstract double currentState();
	
	public void update() {
		double current = currentState(); // encoder.currentEncoderSpeed(); // This is the ONLY line in the ENTIRE code that uses the Encoder.
		double error = target - current;
		double pComponent = error * P / Robot.fastUpdatePeriod;
		double dComponent = (error - previousError) * D / Robot.fastUpdatePeriod;
		previousError = error;
		double iComponent = (integralSum += error) * I / Robot.fastUpdatePeriod;
		motorOutput += pComponent + iComponent + dComponent;
		super.set(motorOutput);
	}
	
	public void disable() {
		super.set(0);
		previousError = 0;
		integralSum = 0;
		motorOutput = 0;
	}
}
