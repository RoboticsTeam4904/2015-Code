package org.usfirst.frc4904.robot.lights.lightsteps;


import org.usfirst.frc4904.robot.lights.EasyLightStep;

public class PartialLights extends EasyLightStep {
	public PartialLights(long durationMillis) {
		super(new RGBRange[] {new RGBRange(175, 167, 252, 0, 73), new RGBRange(29, 190, 222, 74, 103), new RGBRange(175, 167, 252, 104, 178)}, durationMillis);
	}
}