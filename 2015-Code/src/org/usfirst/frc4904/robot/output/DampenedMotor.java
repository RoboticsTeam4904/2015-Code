package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.VictorSP;

public class DampenedMotor extends VictorSP implements Disablable, Updatable {
	private double currentSpeed;
	private double targetSpeed;
	private static final double maxRate = 1; // Per tick. If tick length is changed significantly, adjust this number
	
	public DampenedMotor(int channel) {
		super(channel);
		currentSpeed = 0;
		targetSpeed = 0;
	}
	
	public void setValue(double speed) {
		targetSpeed = speed;
	}
	
	public void update() {
		if (Math.abs(currentSpeed) > 0.25 && Math.abs(targetSpeed) > 0.25 && Math.signum(currentSpeed) == Math.signum(targetSpeed)) {// If speed is greater than 0.5, motors can change fast, so don't dampen.
			currentSpeed = targetSpeed;
		} else {
			if (Math.abs(targetSpeed - currentSpeed) > maxRate) {
				if (targetSpeed > currentSpeed) {
					currentSpeed += maxRate;
				} else if (targetSpeed < currentSpeed) {
					currentSpeed -= maxRate;
				}
			} else {
				currentSpeed = targetSpeed;
			}
		}
		if (currentSpeed != targetSpeed) System.out.println((float) currentSpeed + " | " + (float) targetSpeed);
		super.set(currentSpeed);
	}
	
	public void disable() {
		super.set(0);
		currentSpeed = 0;
		targetSpeed = 0;
	}
}
