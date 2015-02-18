package org.usfirst.frc4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class UDAR implements Updatable {
	private final I2C i2c;
	private static final int NUM_SENSORS = 3;
	private double[] data;
	LogKitten logger;
	
	public UDAR(int i2cPort) {
		i2c = new I2C(Port.kMXP, i2cPort);
		data = new double[3];
		logger = new LogKitten("UDAR", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
	}
	
	public double[] read() {
		return data;
	}
	
	public void update() {
		byte[] I2CData = new byte[NUM_SENSORS];
		i2c.readOnly(I2CData, NUM_SENSORS * 2);
		for (int i = 0; i < NUM_SENSORS; i++) {
			data[i] = new BigInteger(new byte[] {0, I2CData[2 * i], I2CData[2 * i + 1]}).intValue();
		}
	}
}