package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Enablable;
import org.usfirst.frc4904.robot.Overridable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon implements Disablable, Enablable, Overridable<Double>, PIDOutput {
	public static final double MAX_HEIGHT = 9.5; // each level is half a tote height off the bottom-most position of the winch
	private static final double MAX_TICKS = 375.00; // how many pulses from the bottom to the top of the makerslide
	private static final double WINCH_HEIGHT = 58.0; // height of winch makerslide (in inches)
	private static final double HALF_TOTE_HEIGHT = 6.0; // height of half tote (in inches)
	private static final double DISTANCE_PER_PULSE = (WINCH_HEIGHT / HALF_TOTE_HEIGHT) / MAX_TICKS;
	private final Encoder encoder;
	public final DisablablePID pid;
	
	public Winch(int channel, Encoder encoder, double Kp, double Ki, double Kd) {
		super(channel);
		this.encoder = encoder;
		// Resets the encoder.
		encoder.reset();
		// Sets the distance per pulse in inches.
		encoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		// Sets the encoders to use distance for PID.
		// If this is not done, the robot may not go anywhere.
		// It is also possible to use rate, by changing kDistance to kRate.
		encoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
		// Initializes the PID Controller
		pid = new DisablablePID(Kp, Ki, Kd, encoder, this, true);
		pid.setInputRange(0, MAX_HEIGHT);
		pid.setOutputRange(-1, 1);
		pid.setAbsoluteTolerance(0.5);
		pid.disable();
		// SmartDashboard.putNumber("Kp Winch", -0.7);
		// SmartDashboard.putNumber("Ki Winch", 0);
		// SmartDashboard.putNumber("Kd Winch", 0);
	}
	
	public void setHeight(double height) { // Set winch to specific height
		pid.enable();
																																																								pid.setSetpoint(height);
		System.out.println(height);
	}
	
	public void changeHeight(int heightChange) {
		setHeight(pid.getSetpoint() + heightChange);
	}
	
	public double getHeight() {
		return encoder.getDistance();
	}
	
	public void enable() {
		pid.enable();
		pid.disable();
	}
	
	public void disable() {
		set(0);
		pid.setSetpoint(encoder.getDistance());
		pid.disable();
	}
	
	public void override(Double speed) {
		pid.disable();
		pid.setSetpoint(encoder.getDistance());
		super.set(speed);
	}
	
	public void stopOverride() {
		enable();
	}
	
	public boolean onTarget() {
		System.out.println(pid.onTarget());
		return pid.onTarget();
	}
}