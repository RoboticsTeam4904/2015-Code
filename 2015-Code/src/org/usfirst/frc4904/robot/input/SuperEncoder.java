package org.usfirst.frc4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc4904.robot.LogKitten;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class SuperEncoder {
	private final I2C i2c;
	private int previousNumTicks;
	private double previousTime;
	private LogKitten logger;
	
	public SuperEncoder(int i2cPort) {
		logger = new LogKitten("SuperEncoder" + Integer.toString(i2cPort), LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_VERBOSE);
		i2c = new I2C(Port.kOnboard, i2cPort);
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
		logger.v("getTicks", "Tick request");
		byte[] toRecieve = new byte[4];
		byte[] toSend = new byte[1];
		toSend = new byte[] {0};
		logger.v("getTicks", "Begin i2c transaction");
		i2c.transaction(toSend, 1, toRecieve, 4);
		logger.v("getTicks", "End i2c transaction");
		int value = new BigInteger(toRecieve).intValue();
		value = Integer.reverse(value);
		if (value != 0) {
			logger.v("getTicks", Integer.toString(value));
		}
		return value;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
