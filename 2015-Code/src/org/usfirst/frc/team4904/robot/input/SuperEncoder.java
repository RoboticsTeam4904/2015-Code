package org.usfirst.frc.team4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc.team4904.robot.LogKitten;
import edu.wpi.first.wpilibj.I2C;

public class SuperEncoder {
	private final I2C i2c;
	private int previousNumTicks;
	private double previousTime;
	private LogKitten logger;
	
	public SuperEncoder(int i2cAddr) {
		i2c = new I2C(I2C.Port.kOnboard, i2cAddr);
		previousNumTicks = getTicks();
		logger = new LogKitten("SuperEncoder" + Integer.toString(i2cAddr), LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_DEBUG);
	}
	
	public double currentEncoderSpeed() { // Should return ticks per second
		int ticks = getTicks();
		int ticksSincePrev = ticks - previousNumTicks;
		previousNumTicks = ticks;
		double time = time();
		double timeSincePrev = time - previousTime;
		previousTime = time;
		double speed = ticksSincePrev / timeSincePrev;
		logger.v("currentEncoderSpeed", Double.toString(speed));
		return speed / 4500;
	}
	
	private int getTicks() {
		byte[] toRecieve = new byte[4];
		byte[] toSend = new byte[1];
		toSend = new byte[] {0};
		i2c.transaction(toSend, 1, toRecieve, 4);
		int value = new BigInteger(toRecieve).intValue();
		return value;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
