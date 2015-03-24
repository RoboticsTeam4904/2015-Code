package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Enablable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon implements Disablable, Enablable, PIDOutput {
	public static final double MAX_HEIGHT = 9.5; // each level is half a tote height off the bottom-most position of the winch
	private static final double MAX_TICKS = 375.00; // how many pulses from the bottom to the top of the makerslide
	private static final double WINCH_HEIGHT = 58.0; // height of winch makerslide (in inches)
	private static final double HALF_TOTE_HEIGHT = 6.0; // height of half tote (in inches)
	private static final double DISTANCE_PER_PULSE = (WINCH_HEIGHT / HALF_TOTE_HEIGHT) / MAX_TICKS;
	private final Encoder encoder;
	private final PIDController pid;
	private boolean overridePID = false;
	
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
		pid = new PIDController(Kp, Ki, Kd, encoder, this);
		pid.setInputRange(0, MAX_HEIGHT);
		pid.setOutputRange(-1, 1);
		pid.setAbsoluteTolerance(0.5);
		pid.disable();
		// SmartDashboard.putNumber("Kp Winch", -0.7);
		// SmartDashboard.putNumber("Ki Winch", 0);
		// SmartDashboard.putNumber("Kd Winch", 0);
	}
	
	public void setHeight(double height) { // Set winch to specific height
		pid.setSetpoint(height);
	}
	
	public void changeHeight(int heightChange) {
		setHeight(pid.getSetpoint() + heightChange);
	}
	
	public double getHeight() {
		return encoder.getDistance();
	}
	
	public void enable() {
		if (!overridePID) {
			pid.enable();
		}
	}
	
	public void disable() {
		set(0);
		pid.setSetpoint(encoder.getDistance());
		pid.disable();
	}
	
	public void overrideSet(double speed) {
		if (pid.isEnable()) {
			pid.disable();
		} else if (!overridePID) {
			pid.enable();
		}
		if (!pid.isEnable()) {
			super.set(speed);
			pid.setSetpoint(encoder.getDistance());
		}
	}
}