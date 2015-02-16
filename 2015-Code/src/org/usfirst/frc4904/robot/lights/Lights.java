package org.usfirst.frc4904.robot.lights;


import org.usfirst.frc4904.robot.Updatable;

public class Lights implements Updatable {
	protected LightStep[] steps;
	int currentStepIndex = 0;
	
	public void update() {
		if (currentStepIndex < steps.length) {
			LightStep currentStep = steps[currentStepIndex];
			boolean stepCompleted = currentStep.run();
			if (stepCompleted) {
				++currentStepIndex;
			}
		}
	}
}
