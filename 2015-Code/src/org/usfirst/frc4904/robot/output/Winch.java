package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon implements Disablable, Updatable {
	private static final int MAX_HEIGHT = 12; // each level is half a tote height
	private static final double TICK_HEIGHT_RATIO = 28.75; // Some number of ticks is one level. Needs to be determined
	private double targetHeight;
	private double currentHeight;
	public final Encoder encoder;
	
	public Winch(int channel, Encoder encoder) {
		super(channel);
		this.encoder = encoder;
		encoder.reset();
		targetHeight = 0;
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
		setHeight(currentHeight * TICK_HEIGHT_RATIO + heightChange);
	}
	
	public int getHeight() {
		return (int) ((int) currentHeight * TICK_HEIGHT_RATIO);
	}
	
	public void disable() {
		set(0);
		targetHeight = encoder.getDistance() * TICK_HEIGHT_RATIO;
	}
	
	public void update() {
		currentHeight = encoder.getDistance();
		if (currentHeight - targetHeight > 20) {
			// set(1);
		} else if (targetHeight - currentHeight > 20) {
			// set(-1);
		}
		System.out.println(targetHeight + " " + currentHeight);
	}
}