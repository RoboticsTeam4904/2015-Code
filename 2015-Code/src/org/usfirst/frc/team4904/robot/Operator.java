package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public interface Operator {
	
	public void update();
	
	public void raise(int levels);
	public void lower(int levels);
	
	public void grab(int mode);
	public void release(int mode);
	
	public void adjust();
	
}








