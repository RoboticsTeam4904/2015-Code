package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Robot;

public class PID {
	private double P;
	private double I;
	private double D;
	private double integralSum;
	private double previousError;
	
	public PID(double P, double I, double D) {
		this.P = P;
		this.I = I;
		this.D = D;
		reset();
	}
	
	public void reset() {
		previousError = 0;
		integralSum = 0;
	}
	
	public double calculate(double target, double current) {
		double error = target - current;
		double pComponent = error * P / Robot.fastUpdatePeriod;
		double dComponent = (error - previousError) * D / Robot.fastUpdatePeriod;
		previousError = error;
		double iComponent = (integralSum += error) * I / Robot.fastUpdatePeriod;
		return pComponent + iComponent + dComponent;
	}
}
