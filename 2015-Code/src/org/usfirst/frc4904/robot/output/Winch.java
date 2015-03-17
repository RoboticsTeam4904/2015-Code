package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Enablable;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon implements Disablable, Enablable, Updatable, PIDOutput {
	private static final int MAX_HEIGHT = 12; // each level is half a tote height
	private static final double TICK_HEIGHT_RATIO = 28.75; // Some number of ticks is one level. Needs to be determined
	private double targetHeight;
	private double currentHeight;
	public final Encoder encoder;
	private final PIDController pid;
	private final boolean overridePID = false;
	
	public Winch(int channel, Encoder encoder) {
		super(channel);
		this.encoder = encoder;
		encoder.reset();
		targetHeight = 0;
		pid = new PIDController(-1.25F / 180F, -0.001F / 180F, 0, encoder, this);
		if (!overridePID) {
			pid.setContinuous();
			pid.setInputRange(0, 12 * TICK_HEIGHT_RATIO);
			pid.setOutputRange(-1, 1);
			pid.setAbsoluteTolerance(0.5);
		}
	}
	
	public void setHeight(double height) { // Set winch to specific height
		if (height > MAX_HEIGHT) {
			targetHeight = MAX_HEIGHT * TICK_HEIGHT_RATIO;
			return;
		} else if (height < 0) {
			targetHeight = 0;
		}
	}
	
	public void changeHeight(int heightChange) {
		setHeight(currentHeight + heightChange);
	}
	
	public int getHeight() {
		return (int) ((int) currentHeight * TICK_HEIGHT_RATIO);
	}
	
	public void enable() {
		pid.enable();
	}
	
	public void disable() {
		set(0);
		pid.disable();
		targetHeight = encoder.getDistance() * TICK_HEIGHT_RATIO;
	}
	
	public void update() {
		pid.setSetpoint(targetHeight);
		System.out.println(targetHeight + " " + currentHeight);
	}
}