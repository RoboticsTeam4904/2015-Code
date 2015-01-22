package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public interface Operator {
	
	public void update(); // Call once per cycle to update
	
	abstract void raise(int levels); // Raise winch levels levels
	abstract void lower(int levels); // Lower winch levels levels
	
	abstract void grab(int mode); // Grab a can/tote
	abstract void release(int mode); // Release a can/tote
	
	abstract void adjust(); // Adjust the winch
	
}








