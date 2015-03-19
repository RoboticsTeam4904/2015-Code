package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Enablable;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon implements Disablable, Enablable, Updatable, PIDOutput {
	private static final double MAX_HEIGHT = 12; // each level is half a tote height
	private static final double DISTANCE_PER_PULSE = 1.0 / 28.75; // Some number of ticks is one level. Needs to be determined
	private final Encoder encoder;
	private final PIDController pid;
	private final boolean overridePID = false;
	
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
	}
	
	public void setHeight(double height) { // Set winch to specific height
		if (height > MAX_HEIGHT) {
			pid.setSetpoint(MAX_HEIGHT);
		} else if (height < 0) {
			pid.setSetpoint(0);
		} else {
			pid.setSetpoint(height);
		}
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
	
	public void update() {
		if (overridePID) {
			pid.disable();
			if (encoder.getDistance() < pid.getSetpoint()) {
				set(1);
			} else if (encoder.getDistance() > pid.getSetpoint()) {
				set(-1);
			} else {
				set(0); // will never happen but whatevs
			}
		}
	}
}