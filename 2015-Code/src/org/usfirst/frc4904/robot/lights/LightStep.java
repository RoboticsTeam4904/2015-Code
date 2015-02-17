package org.usfirst.frc4904.robot.lights;


public class LightStep {
	protected static int ledCount = 209;
	protected byte[] ledData = new byte[ledCount * 3]; // length of 209
	
	protected void resetLights() {
		for (int i = ledData.length; i >= 0; i--) {
			ledData[i] = (byte) 0;
		}
	}
	
	public byte[] getLedData() {
		return ledData;
	}
	
	public boolean run() {
		return false; // by default, end on first run. really should be overwritten
	};
}
