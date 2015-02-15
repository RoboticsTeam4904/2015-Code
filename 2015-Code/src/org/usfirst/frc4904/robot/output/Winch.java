package org.usfirst.frc4904.robot.output;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.input.SuperEncoder;

public class Winch extends PositionEncodedMotor implements Disablable {
	private static final int MAX_HEIGHT = 12; // each level is half a tote height
	private static final double TICK_HEIGHT_RATIO = 100; // Some number of ticks is one level. Needs to be determined
	private int currentHeight;
	
	public Winch(int channel, SuperEncoder encoder) {
		super(channel, encoder, new PID(1.0, 0.0, 0.3));
		currentHeight = 0;
	}
	
	public void setHeight(int height) { // Set winch to specific height
		if (height > MAX_HEIGHT) {
			currentHeight = MAX_HEIGHT;
			return;
		} else if (height < 0) {
			height = 0;
		}
		super.setValue(height * TICK_HEIGHT_RATIO);
		currentHeight = (int) (super.currentState() / TICK_HEIGHT_RATIO);
	}
	
	public void changeHeight(int heightChange) {
		setHeight(currentHeight + heightChange);
	}
	
	public int getHeight() {
		return currentHeight;
	}
	
	public void disable() {
		setSpeed(0);
		target = currentHeight * TICK_HEIGHT_RATIO;
	}
}