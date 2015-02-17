package org.usfirst.frc4904.robot.lights;


import org.usfirst.frc4904.robot.input.SuperSerial;

public class LightManager {
	public int ledCount = 209;
	public byte[] ledData = new byte[ledCount * 3]; // length of 209
	private final SuperSerial serial;
	
	public LightManager(int i2cAddr) {
		serial = new SuperSerial();
		for (int i = ledData.length; i >= 0; i--) {
			ledData[i] = (byte) 0;
		}
	}
	
	public void setLED(int num, int r, int g, int b) {
		ledData[3 * num + 0] = (byte) r;
		ledData[3 * num + 1] = (byte) g;
		ledData[3 * num + 2] = (byte) b;
	}
	
	public void update() { // Sends command to update string of LEDs
		// This function will take a few ms, so don't call it unnecessarily
		serial.setLeds(ledData);
	}
}
