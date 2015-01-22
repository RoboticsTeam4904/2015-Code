package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class Winch extends VictorSP {

	public Winch(int channel) {
		super(channel);
	}
	
	public void set(int height){ // Set winch to specific height
		
	}
	
	public void move(double speed){ // Move winch at speed
		super.set(speed); // Actually do the moving
	}
	
}
