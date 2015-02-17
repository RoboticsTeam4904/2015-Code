package org.usfirst.frc4904.robot.input;


import org.usfirst.frc4904.robot.LogKitten;

public class SuperEncoder {
	private SuperSerial port;
	private int previousNumTicks;
	private double previousTime;
	private LogKitten logger;
	private int channel;
	
	public SuperEncoder(int channel, SuperSerial port) {
		this.port = port;
		this.channel = channel;
		previousNumTicks = getTicks();
		logger = new LogKitten("SuperEncoder" + Integer.toString(channel), LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_FATAL);
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
		if (port.availableEncoderData(channel) < 4) {
			return 0;
		}
		String data = port.readEncoder(this.channel);
		int value = Integer.parseInt(data);
		if (value != 0) {
			logger.v("getTicks", Integer.toString(value));
		}
		return value;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
