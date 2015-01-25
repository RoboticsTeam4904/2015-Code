package org.usfirst.frc.team4904.robot.output;


import edu.wpi.first.wpilibj.Talon;

public class Winch extends Talon {
	private static transient final int MAX_HEIGHT = 10;
	
	public Winch(int channel) {
		super(channel);
	}
	
	public void set(int height) { // Set winch to specific height
		if (height > MAX_HEIGHT) height = MAX_HEIGHT;
		else if (height < 0) height = 0;
	}
	
	public void move(double speed) { // Move winch at speed
		super.set(speed); // Actually do the moving
	}
}