package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Disablable;
import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon implements Disablable {
	private static final int MAX_HEIGHT = 12;
	private int currentHeight;
	
	public Winch(int channel) {
		super(channel);
		currentHeight = 0;
	}
	
	public void setHeight(int height, boolean absolute) { // Set winch to specific height
		if (!absolute) {
			if (currentHeight + height > MAX_HEIGHT) {
				currentHeight = MAX_HEIGHT;
				return;
			} else if (currentHeight + height < 0) {
				currentHeight = 0;
				return;
			}
			currentHeight += height;
		} else {
			if (height > MAX_HEIGHT) {
				currentHeight = MAX_HEIGHT;
				return;
			} else if (height < 0) {
				currentHeight = 0;
				return;
			}
			currentHeight = height;
		}
	}
	
	public void disable() {
		set(0);
	}
}