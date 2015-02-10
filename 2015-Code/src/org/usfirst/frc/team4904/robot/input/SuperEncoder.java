package org.usfirst.frc.team4904.robot.input;


import java.math.BigInteger;
import edu.wpi.first.wpilibj.I2C;

public class SuperEncoder {
	private final I2C i2c;
	private int previousNumTicks;
	private double previousTime;
	
	public SuperEncoder(int i2cAddr) {
		i2c = new I2C(I2C.Port.kOnboard, i2cAddr);
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
		return speed / 4500;
	}
	
	public int getTicks() {
		byte[] toRecieve = new byte[4];
		// i2c.transaction(null, 0, toRecieve, 4);
		return new BigInteger(toRecieve).intValue();
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
