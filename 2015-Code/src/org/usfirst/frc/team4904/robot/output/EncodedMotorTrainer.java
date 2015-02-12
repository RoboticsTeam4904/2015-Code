package org.usfirst.frc.team4904.robot.output;


public class EncodedMotorTrainer {
	private boolean doTrain;
	private double currentP;
	private double currentI;
	private double currentD;
	private double previousError;
	private int mode;
	private int errorDelay;
	public static final int TRAINING_P = 0;
	public static final int TRAINING_I = 1;
	public static final int TRAINING_D = 2;
	private static final int TRAINING_PERIOD = 50;
	
	public EncodedMotorTrainer(double P, double I, double D, boolean doTrain) {
		this.doTrain = doTrain;
		errorDelay = TRAINING_PERIOD;
	}
	
	public boolean doTrain() {
		if (errorDelay > 0) {
			errorDelay--;
			return false;
		}
		errorDelay = TRAINING_PERIOD;
		return doTrain;
	}
	
	public int getMode() {
		return mode;
	}
	
	public double getNew(double P, double I, double D, double error) {
		double adjust = 0.001;
		int var = mode;
		if (error < previousError) {
			adjust = adjust * -1;
			mode++;
		}
		switch (var) {
			case TRAINING_P:
				return currentP + adjust;
			case TRAINING_I:
				return currentI + adjust;
			case TRAINING_D:
				return currentD + adjust;
			default:
				return 0;
		}
	}
}
