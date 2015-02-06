package org.usfirst.frc.team4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc.team4904.robot.SuperI2C;
import edu.wpi.first.wpilibj.I2C;

public class UDAR extends SuperI2C {
	private final int numSensors = 3;
	
	public UDAR() {
		i2c = new I2C(I2C.Port.kOnboard, 2); // Initialize I2C
		data = new double[3];
	}
	
	private int bytesCurrentlyAvailable() {
		return 0;
	}
	
	public void update() {
		if (bytesCurrentlyAvailable() < numSensors * 2) {
			byte[] I2CData = new byte[numSensors];
			i2c.readOnly(I2CData, numSensors * 2);
			for (int i = 0; i < numSensors; i++) {
				data[i] = new BigInteger(new byte[] {0, I2CData[2 * i], I2CData[2 * i + 1]}).intValue();
			}
		}
	}
	
	public void disable() {}
}