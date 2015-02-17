package org.usfirst.frc4904.robot.lights;


import org.usfirst.frc4904.robot.input.SuperSerial;
import org.usfirst.frc4904.robot.lights.lightsteps.PartialLights;

public class MasterLightSequence extends Lights {
	public MasterLightSequence(SuperSerial serial) {
		// super(new EasyLightStep[] {new BlueLights(5 * 1000), new PurpleLights(5 * 1000)}, 3, serial);
		super(new EasyLightStep[] {new PartialLights(500)}, 10, serial);
		// blue for 6 seconds, then purple for 5 seconds | 3 times
	}
}
