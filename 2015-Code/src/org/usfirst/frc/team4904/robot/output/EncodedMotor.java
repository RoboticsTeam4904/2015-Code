package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.input.SuperEncoder;
import edu.wpi.first.wpilibj.VictorSP;

public abstract class EncodedMotor extends VictorSP implements Disablable, Updatable {
	protected volatile double target;
	private PID pid;
	private double motorOutput;
	protected double P = 1.0; // ticks per second Assuming this is updated at 200Hz, this should result in 0.5 seconds to full speed
	protected double I = 0.3; // ticks
	protected double D = 0.3; // ticks per second per second
	protected final SuperEncoder encoder;
	
	public EncodedMotor(int channel, SuperEncoder encoder, PID pid) {
		super(channel);
		this.encoder = encoder;
		this.pid = pid;
		motorOutput = 0;
	}
	
	public abstract void set(double value);
	
	protected abstract double currentState();
	
	public void update() {
		motorOutput += pid.calculate(target, currentState());
		super.set(motorOutput);
	}
	
	public void disable() {
		super.set(0);
		pid.reset();
		motorOutput = 0;
	}
}
