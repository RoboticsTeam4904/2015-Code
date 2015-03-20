package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Enablable;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Winch extends Talon implements Disablable, Enablable, Updatable, PIDOutput {
	private static final double MAX_HEIGHT = 12; // each level is half a tote height off the bottom-most position of the winch
	private static final double DISTANCE_PER_PULSE = (58.0 / 6.0) / 344.00; // Makerslide height: 57-58". Half-tote: 6". Winch pulses per makerslide: 344.
	private final Encoder encoder;
	private final PIDController pid;
	private boolean overridePID = true;
	
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
		pid = new PIDController(Kp, Ki, Kd, encoder, new PIDVariable());
		pid.setInputRange(0, MAX_HEIGHT);
		pid.setOutputRange(-1, 1);
		pid.setAbsoluteTolerance(0.5);
		pid.disable();
		SmartDashboard.putNumber("Kp Winch", 0);
		SmartDashboard.putNumber("Ki Winch", 0);
		SmartDashboard.putNumber("Kd Winch", 0);
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
		pid.setPID(SmartDashboard.getNumber("Kp Winch"), SmartDashboard.getNumber("Ki Winch"), SmartDashboard.getNumber("Kd Winch"));
		if (overridePID) {}
	}
	
	public void overrideSet(double speed) {
		if (Math.abs(speed) > 0.05 && pid.isEnable()) {
			pid.disable();
		} else if (Math.abs(speed) < 0.05 && !overridePID) {
			pid.enable();
		}
		if (!pid.isEnable()) {
			super.set(speed);
		}
	}
}