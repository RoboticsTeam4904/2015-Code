package org.usfirst.frc.team4904.robot.lights;


import org.usfirst.frc.team4904.robot.Updatable;

public class LightSet implements Updatable {
	private LightManager lights;
	private long lastUpdate = System.currentTimeMillis();
	
	private enum COLOR {
		GREEN, TURQUOISE
	}
	private COLOR lastColor = COLOR.GREEN;
	
	public LightSet() {
		this.lights = new LightManager(4);
	}
	
	public void setAll(int r, int g, int b) {
		for (int i = 0; i < lights.leds; i++) {
			lights.setLED(i, r, g, b);
		}
		lights.update();
	}
	
	public void update() {
		if (System.currentTimeMillis() < (lastUpdate + 2 * 1000)) {
			if (lastColor == COLOR.GREEN) {
				setAll(29, 190, 222);
				lastColor = COLOR.TURQUOISE;
			} else if (lastColor == COLOR.TURQUOISE) {
				setAll(160, 242, 182);
				lastColor = COLOR.GREEN;
			}
			lastUpdate = System.currentTimeMillis();
		}
	}
}
