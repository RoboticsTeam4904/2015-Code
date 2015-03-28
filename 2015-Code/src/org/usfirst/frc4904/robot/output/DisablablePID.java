package org.usfirst.frc4904.robot.output;


import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class DisablablePID extends PIDController {
	private final boolean enabled; // This acts as an override for PIDController. Setting this to false will prevent the controller from activating or doing anything.
	
	/**
	 * The DisablablePID class allows for code switches to kill PID.
	 * The class has a one-off switch at construction time to kill the underlying PIDController for all eternity.
	 * 
	 * @param Kp
	 *        P constant for PID
	 * @param Ki
	 *        I constant for PID
	 * @param Kd
	 *        D constant for PID
	 * @param source
	 *        Input source for PID loop
	 * @param output
	 *        Output device for PID loop
	 * @param enabled
	 *        Whether the PID controller is enabled or not
	 */
	public DisablablePID(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, boolean enabled) {
		super(Kp, Ki, Kd, source, output);
		this.enabled = enabled;
	}
	
	public void enable() {
		// Only enable PIDController if we are not disabled
		if (enabled) {
			super.enable();
		}
	}
}
