package org.usfirst.frc.team4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.I2C;

public class UDAR implements Updatable {
	private I2C i2c;
	private final int numSensors = 3;
	int[] dists = new int[numSensors];
	
	public UDAR() {
		this.i2c = new I2C(I2C.Port.kOnboard, 2); // Initialize I2C
	}
	
	public int[] getDists() {
		return dists;
	}
	
	private int bytesCurrentlyAvailable() {
		return 0;
	}
	
	public void update() {
		if (bytesCurrentlyAvailable() < numSensors * 2) {
			byte[] data = new byte[numSensors];
			i2c.readOnly(data, numSensors * 2);
			for (int i = 0; i < numSensors; i++) {
				dists[i] = new BigInteger(new byte[] {0, data[2 * i], data[2 * i + 1]}).intValue();
			}
		}
	}
}