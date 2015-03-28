package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Enablable;
import org.usfirst.frc4904.robot.Overridable;

public class EncodedMotor extends Talon implements Disablable, Enablable, Overridable<Double>, PIDOutput {
	private final Encoder encoder;
	private final DisablablePID pid;
	private final static boolean ENABLED = false;
	
	public EncodedMotor(int channel, Encoder encoder, double Kp, double Ki, double Kd) {
		super(channel);
		this.encoder = encoder;
		// Zero the encoder.
		encoder.reset();
		// Set the encoders to use rate for PID.
		encoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
		// Initialize and configure the PID Controller.
		pid = new DisablablePID(Kp, Ki, Kd, encoder, this, ENABLED);
		pid.setOutputRange(-1, 1);
		pid.setAbsoluteTolerance(0.1);
		// Make sure the PID doesn't start out enabled
		pid.disable();
	}
	
	/**
	 * Set the desired speed for the motor.
	 * 
	 * @param speed
	 *        A double from -1 to 1 representing the desired speed
	 */
	public void set(double speed) {
		pid.setSetpoint(speed);
	}
	
	public void enable() {
		pid.enable();
	}
	
	public void disable() {
		set(0);
		pid.setSetpoint(0);
		pid.disable();
	}
	
	public void override(Double speed) {
		pid.disable();
		super.set(speed);
		pid.setSetpoint(speed); // Make sure that motor maintains speed when enabled
	}
	
	public void stopOverride() {
		enable();
	}
}