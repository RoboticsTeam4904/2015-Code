package org.usfirst.frc4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc4904.robot.SuperPort;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.I2C;

public class UDAR extends SuperPort implements Updatable {
	private I2C i2c;
	private final int numSensors = 3;
	
	public UDAR() {
		try {
			i2c = new I2C(I2C.Port.kOnboard, 2); // Initialize I2C
		}
		catch (NullPointerException e) {
			i2c = null;
		}
		data = new double[3];
	}
	
	private int bytesCurrentlyAvailable() {
		return 0;
	}
	
	public void update() {
		if (i2c == null) {
			return;
		}
		try {
			if (bytesCurrentlyAvailable() < numSensors * 2) {
				byte[] I2CData = new byte[numSensors];
				i2c.readOnly(I2CData, numSensors * 2);
				for (int i = 0; i < numSensors; i++) {
					data[i] = new BigInteger(new byte[] {0, I2CData[2 * i], I2CData[2 * i + 1]}).intValue();
				}
			}
		}
		catch (NullPointerException e) {
			for (int i = 0; i < 3; i++) {
				data[i] = -1;
			}
		}
	}
}