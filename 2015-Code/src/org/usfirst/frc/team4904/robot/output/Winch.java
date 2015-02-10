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
	
<<<<<<< HEAD
	public void setHeight(int height) { // Set winch to specific height
		if (height > MAX_HEIGHT) {
			height = MAX_HEIGHT;
		} else if (height < 0) {
			height = 0;
=======
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
>>>>>>> 100378801a3470a0283a9954a120a617ca840a74
		}
	}
	
<<<<<<< HEAD
	public void changeHeight(int heightChange) {
		setHeight(currentHeight + heightChange);
	}
	
=======
>>>>>>> 100378801a3470a0283a9954a120a617ca840a74
	public void disable() {
		set(0);
	}
}