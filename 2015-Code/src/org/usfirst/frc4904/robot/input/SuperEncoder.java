package org.usfirst.frc4904.robot.input;


import org.usfirst.frc4904.robot.LogKitten;
import edu.wpi.first.wpilibj.Encoder;

public class SuperEncoder {
	public final Encoder encoder;
	private int previousNumTicks;
	private double previousTime;
	private LogKitten logger;
	
	public SuperEncoder(int port1, int port2) {
		logger = new LogKitten("SuperEncoder" + Integer.toString(port1), LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_DEBUG);
		encoder = new Encoder(port1, port2);
		encoder.reset();
		previousNumTicks = getTicks();
	}
	
	public double currentEncoderSpeed() { // Should return ticks per second
		int ticks = getTicks();
		int ticksSincePrev = ticks - previousNumTicks;
		previousNumTicks = ticks;
		double time = time();
		double timeSincePrev = time - previousTime;
		previousTime = time;
		double speed = ticksSincePrev / timeSincePrev;
		if (speed != 0.0) {
			logger.v("currentEncoderSpeed", Double.toString(speed));
		}
		return speed / 4500;
	}
	
	public int getTicks() {
		int value = encoder.get();
		// value = Integer.reverse(value);
		logger.v("getTicks", Integer.toString(value));
		return value;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
