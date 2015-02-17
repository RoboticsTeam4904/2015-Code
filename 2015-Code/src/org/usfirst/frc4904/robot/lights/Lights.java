package org.usfirst.frc4904.robot.lights;


import java.util.Arrays;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.input.SuperSerial;

public class Lights implements Updatable {
	protected LightStep[] steps;
	int currentStepIndex = 0;
	public boolean disabled = false;
	protected int iter;
	public int ledCount = 209;
	public byte[] previousLedData = new byte[ledCount * 3];
	public byte[] ledData = new byte[ledCount * 3]; // length of 209 with R,G,B for each
	private SuperSerial serial;
	
	private void init(LightStep[] steps, SuperSerial serial) {
		this.steps = steps;
		this.serial = serial;
		for (int i = ledData.length; i >= 0; i--) {
			ledData[i] = (byte) 0; // zero out the LEDs
			previousLedData[i] = (byte) 0;
		}
	}
	
	public Lights(LightStep[] steps, boolean infinite, SuperSerial serial) {
		init(steps, serial);
		if (infinite) {
			this.iter = -1;
		} else {
			this.iter = 1;
		}
	}
	
	public Lights(LightStep[] steps, int iterations, SuperSerial serial) {
		init(steps, serial);
		this.iter = Math.abs(iterations); // prevent infinite loops from this constructor (on principle)
	}
	
	public void update() {
		if (iter == 0) { // if all iterations are complete or infinite (less than 0)
			return;
		}
		if (currentStepIndex < steps.length) {
			LightStep currentStep = steps[currentStepIndex];
			boolean stepCompleted = currentStep.run();
			previousLedData = ledData;
			ledData = currentStep.getLedData();
			if (!Arrays.equals(previousLedData, ledData)) {
				serial.setLeds(ledData);
			}
			if (stepCompleted) {
				++currentStepIndex;
			}
		} else {
			currentStepIndex = 0;
			--iter;
		}
	}
}
