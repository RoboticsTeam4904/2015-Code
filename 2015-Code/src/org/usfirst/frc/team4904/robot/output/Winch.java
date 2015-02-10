package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Disablable;
import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon implements Disablable {
	private static final int MAX_HEIGHT = 12; // each level is half a tote height
	private int currentHeight;
	
	public Winch(int channel) {
		super(channel);
		currentHeight = 0;
	}
	
	public void setHeight(int height) { // Set winch to specific height
		if (height > MAX_HEIGHT) {
			height = MAX_HEIGHT;
		} else if (height < 0) {
			height = 0;
		}
		currentHeight = height;
	}
	
	public int getHeight() {
		return currentHeight;
	}
	
	public void disable() {
		set(0);
	}
}