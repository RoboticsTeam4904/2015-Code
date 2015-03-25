package org.usfirst.frc4904.robot.output;


import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class DisablablePID extends PIDController {
	private final boolean enabled;
	
	public DisablablePID(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, boolean enabled) {
		super(Kp, Ki, Kd, source, output);
		this.enabled = enabled;
	}
	
	public void enable() {
		if (enabled) {
			super.enable();
		}
	}
}
