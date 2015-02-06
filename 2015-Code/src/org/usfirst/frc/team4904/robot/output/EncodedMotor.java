package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;

public class EncodedMotor extends VictorSP implements Updatable {
	private volatile double target;
	private volatile double derivative;
	private volatile double integralSum;
	private volatile double prev;
	private final Encoder encoder;
	private final double P = 0.01;
	private final double I = 0.01;
	private final double D = 0.01;
	
	public EncodedMotor(int channel, Encoder encoder) {
		super(channel);
		this.encoder = encoder;
		// TODO Auto-generated constructor stub
	}
	
	public void set(double value) {
		integralSum = 0;
		target = value;
	}
	
	public void update() {
		double speed = currentEncoderSpeed();
		double error = target - speed;
		double pComponent = error * P;
		double dComponent = (error - derivative) * D;
		derivative = error;
		double iComponent = (integralSum += error) * I;
		prev += pComponent + iComponent + dComponent;
		super.set(prev);
	}
	
	private double currentEncoderSpeed() {
		return encoder.getRate();
	}
}
