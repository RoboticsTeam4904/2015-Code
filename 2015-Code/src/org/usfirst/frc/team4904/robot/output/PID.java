package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.LogKitten;
import org.usfirst.frc.team4904.robot.Robot;

public class PID {
	private double P;
	private double I;
	private double D;
	private double integralSum;
	private double previousError;
	private double absoluteError;
	private double lastAbsoluteError;
	private LogKitten logger;
	private double[] lastPID;
	
	public PID(double P, double I, double D) {
		this.P = P;
		this.I = I;
		this.D = D;
		logger = new LogKitten("PID", LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_VERBOSE);
		reset();
	}
	
	public void reset() {
		previousError = 1;
		integralSum = 0;
		absoluteError = 1;
	}
	
	public double calculate(double target, double current) {
		double error = (target - current) / Robot.fastUpdatePeriod;
		double pComponent = error * P;
		double dComponent = (error - previousError) * D;
		previousError = error;
		integralSum += error;
		double iComponent = integralSum * I;
		// Update total error
		absoluteError += Math.abs(error);
		if (current != 0.0) {
			logger.v("Current", Double.toString(current));
		}
		return pComponent + iComponent + dComponent;
	}
	
	public void train() {
		logger.v("EndTrainP", Double.toString(P));
		logger.v("EndtrainI", Double.toString(I));
		logger.v("EndtrainD", Double.toString(D));
		logger.v("EndtrainError", Double.toString(absoluteError));
		double[] newPID = new double[3];
		double errorDiff = lastAbsoluteError - absoluteError;
		// Only train P coefficient
		newPID[0] = (errorDiff * P * 0.1) + P;
		newPID[1] = I; // errorDiff * I * 0.1;
		newPID[2] = D; // errorDiff * D * 0.1;
		lastPID = new double[] {P, I, D};
		lastAbsoluteError = absoluteError;
		P = newPID[0];
		I = newPID[1];
		D = newPID[2];
		reset();
	}
}
