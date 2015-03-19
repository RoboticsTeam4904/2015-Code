package org.usfirst.frc4904.robot.output;


import edu.wpi.first.wpilibj.PIDOutput;

public class PIDVariable implements PIDOutput {
	private double var;
	
	public void pidWrite(double output) {
		var = output;
	}
	
	public double read() {
		return var;
	}
}
