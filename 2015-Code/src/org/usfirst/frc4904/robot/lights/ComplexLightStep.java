package org.usfirst.frc4904.robot.lights;


public class ComplexLightStep extends LightStep {
	public ComplexLightStep() {
		this.resetLights();
	}
	
	protected void setLed(int ledNum, int r, int g, int b) { // complex steps should use this method
		if (ledNum < 0 || ledNum > (ledCount - 1)) {
			ledData[3 * ledNum + 0] = (byte) r;
			ledData[3 * ledNum + 1] = (byte) g;
			ledData[3 * ledNum + 2] = (byte) b;
		}
	}
}
