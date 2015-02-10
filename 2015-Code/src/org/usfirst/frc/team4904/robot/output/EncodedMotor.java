package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.Robot;
import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.input.SuperEncoder;
import edu.wpi.first.wpilibj.VictorSP;

public class EncodedMotor extends VictorSP implements Disablable, Updatable {
	private volatile double target;
	private volatile double previousError;
	private volatile double integralSum;
	private volatile double motorOutput;
	private final double P = 1 / Robot.fastUpdatePeriod; // ticks per second Assuming this is updated at 200Hz, this should result in 0.5 seconds to full speed
	private final double I = 0.3 * P; // ticks
	private final double D = 0.3 * P; // ticks per second per second
	private final SuperEncoder encoder;

	public EncodedMotor(int channel, SuperEncoder encoder) {
		super(channel);
		this.encoder = encoder;
		target = 0;
		previousError = 0;
		integralSum = 0;
		motorOutput = 0;
	}

	public void set(double value) {
		integralSum = 0;
		target = value;
		if (target > 1) {
			target = 1;
		}
		if (target < -1) {
			target = -1;
		}
	}

	public void update() {
		double speed = encoder.currentEncoderSpeed(); // This is the ONLY line in the ENTIRE code that uses the Encoder.
		double error = target - speed;
		double pComponent = error * P;
		double dComponent = (error - previousError) * D;
		previousError = error;
		double iComponent = (integralSum += error) * I;
		motorOutput += pComponent + iComponent + dComponent;
		super.set(motorOutput);
	}

	public void disable() {
		super.set(0);
		target = 0;
		previousError = 0;
		integralSum = 0;
		motorOutput = 0;
	}
}
