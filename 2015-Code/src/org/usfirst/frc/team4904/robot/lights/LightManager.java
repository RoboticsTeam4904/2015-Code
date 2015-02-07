package org.usfirst.frc.team4904.robot.lights;


import org.usfirst.frc.team4904.robot.SuperI2C;
import edu.wpi.first.wpilibj.I2C;

public class LightManager extends SuperI2C {
	public int leds = 300;
	private final I2C i2c;

	public LightManager(int i2cAddr) {
		this.i2c = new I2C(I2C.Port.kOnboard, i2cAddr);
	}

	public void setLED(byte num, byte r, byte g, byte b) {
		byte[] data = new byte[4];
		data[0] = num;
		data[1] = r;
		data[2] = g;
		data[3] = b;
		i2c.transaction(data, 4, null, 0);
	}

	public void update() { // Sends command to update string of LEDs
		// This function will take a few ms, so don't call it unnecessarily
		byte[] data = new byte[1];
		data[0] = 'u';
		i2c.transaction(data, 1, null, 0);
	}

	public void setLED(int num, int r, int g, int b) {
		this.setLED((byte) num, (byte) r, (byte) g, (byte) b);
	}
}
