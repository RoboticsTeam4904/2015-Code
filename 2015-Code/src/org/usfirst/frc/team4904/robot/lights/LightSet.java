package org.usfirst.frc.team4904.robot.lights;


public class LightSet {
	private LightManager lights;
	
	public LightSet() {
		this.lights = new LightManager(4);
	}
	
	public void allBlue() {
		for (int i = 0; i < lights.leds; i++) {
			lights.setLED(i, 0, 0, 255);
		}
		lights.update();
	}
}
