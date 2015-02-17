package org.usfirst.frc4904.robot.lights;


import org.usfirst.frc4904.robot.input.SuperSerial;
import org.usfirst.frc4904.robot.lights.lightsteps.BlueLights;
import org.usfirst.frc4904.robot.lights.lightsteps.PurpleLights;

public class DemoLightSequence extends Lights {
	public DemoLightSequence(SuperSerial serial) {
		super(new EasyLightStep[] {new BlueLights(5 * 1000), new PurpleLights(5 * 1000)}, 3, serial);
		// blue for 6 seconds, then purple for 5 seconds | 3 times
	}
}
