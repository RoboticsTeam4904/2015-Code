package org.usfirst.frc.team4904.robot.input;


<<<<<<< HEAD
=======
import java.math.BigInteger;
>>>>>>> 93a93a809166ed763774a9d86cd78d8e458de824
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
		logger = new LogKitten("SuperEncoder", LogKitten.LEVEL_DEBUG);
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
		// byte[] toRecieve = new byte[4];
		// i2c.transaction(null, 0, toRecieve, 4);
		// logger.d("getTicks", toRecieve.toString());
		// return new BigInteger(toRecieve).intValue();
		return 0;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
}
