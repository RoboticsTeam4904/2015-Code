package org.usfirst.frc.team4904.robot;

public interface Driver {
	
	public void update();
	
	abstract void move(double speed, double angle);
	abstract void turn(double speed);
	
}
