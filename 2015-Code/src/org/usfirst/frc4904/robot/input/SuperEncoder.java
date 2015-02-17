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
		i2c = new I2C(Port.kOnboard, i2cPort);
		previousNumTicks = getTicks();
		logger = new LogKitten("SuperEncoder" + Integer.toString(i2cPort), LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_FATAL);
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
		byte[] toRecieve = new byte[4];
		byte[] toSend = new byte[1];
		toSend = new byte[] {0};
		i2c.transaction(toSend, 1, toRecieve, 4);
		int value = new BigInteger(toRecieve).intValue();
		if (value != 0) {
			try {
				logger.v("getTicks", Integer.toString(value));
			}
			catch (Exception e) {
				value = 0;
			}
		}
		return value;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
