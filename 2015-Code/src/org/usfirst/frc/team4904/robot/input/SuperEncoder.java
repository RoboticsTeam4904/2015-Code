package org.usfirst.frc.team4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc.team4904.robot.LogKitten;
import edu.wpi.first.wpilibj.I2C;

public class SuperEncoder {
	private I2C i2c;
	private int previousNumTicks;
	private double previousTime;
	private LogKitten logger;
	
	public SuperEncoder(int i2cAddr) {
		try {
			i2c = new I2C(I2C.Port.kOnboard, i2cAddr);
		}
		catch (NullPointerException e) {
			i2c = null;
		}
		previousNumTicks = getTicks();
		logger = new LogKitten("SuperEncoder" + Integer.toString(i2cAddr), LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_VERBOSE);
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
		if (i2c == null) {
			return 0;
		}
		int value;
		try {
			byte[] toRecieve = new byte[4];
			byte[] toSend = new byte[1];
			toSend = new byte[] {0};
			i2c.transaction(toSend, 1, toRecieve, 4);
			value = new BigInteger(toRecieve).intValue();
		}
		catch (NullPointerException e) {
			value = 0;
		}
		return value;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
