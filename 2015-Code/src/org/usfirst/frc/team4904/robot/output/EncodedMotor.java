package org.usfirst.frc.team4904.robot.output;


import java.math.BigInteger;
import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.VictorSP;

public class EncodedMotor extends VictorSP implements Updatable {
	private volatile double target;
	private volatile double previousError;
	private volatile double integralSum;
	private volatile double motorOutput;
	private final double P = 0.01;// ticks per second Assuming this is updated at 200Hz, this should result in 0.5 seconds to full speed
	private final double I = 0.003;// ticks
	private final double D = 0.003;// ticks per second per second
	private final I2C encoder;
	private int previousNumTicks;
	private double previousTime;
	
	public EncodedMotor(int channel, int i2cAddr) {
		super(channel);
		this.encoder = new I2C(I2C.Port.kOnboard, i2cAddr);
		previousNumTicks = getTicks();
		previousTime = time();
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
		double speed = currentEncoderSpeed();
		double error = target - speed;
		double pComponent = error * P;
		double dComponent = (error - previousError) * D;
		previousError = error;
		double iComponent = (integralSum += error) * I;
		motorOutput += pComponent + iComponent + dComponent;
		super.set(motorOutput);
	}

	private double currentEncoderSpeed() {// Should return ticks per second
		int ticks = getTicks();
		int ticksSincePrev = ticks - previousNumTicks;
		previousNumTicks = ticks;
		double time = time();
		double timeSincePrev = time - previousTime;
		previousTime = time;
		double speed = ticksSincePrev / timeSincePrev;
		return speed / 4500;
	}
	
	private int getTicks() {
		byte[] toRecieve = new byte[4];
		encoder.transaction(null, 0, toRecieve, 4);
		return new BigInteger(toRecieve).intValue();
	}

	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
	
	public void disable() {
		super.set(0);
		previousNumTicks = getTicks();
		previousTime = time();
		target = 0;
		previousError = 0;
		integralSum = 0;
		motorOutput = 0;
	}

	public double getRumble() {
		return Math.abs(previousError) / 2;
	}
}
