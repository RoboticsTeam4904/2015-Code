package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class OperatorNachi extends Operator {
	
	boolean holdingTote;
	boolean holdingCan;
	
	public OperatorNachi(LogitechJoystick stick, Winch winch, AutoAlign align){
		super(stick, winch, align);
		
		holdingTote = false;
		holdingCan = false;
	}
	
	public void update() {
		if (stick.x1.get()){ // When button 1 is pressed, toggle tote grabbing
			if(!holdingTote) grab(MODE_WIDE_TOTE);
			else release(MODE_WIDE_TOTE);
			holdingTote = !holdingTote;
		}
		if (stick.x2.get()){ // When button 2 is pressed, toggle can grabbing
			if(!holdingCan) grab(MODE_CAN);
			else release(MODE_CAN);
			holdingCan = !holdingCan;
		}
		if (stick.x4.get()) raise(1); // When button 4 is pressed, raise the winch one level
		if (stick.x3.get()) lower(1); // When button 3 is pressed, lower the winch one level
		if (stick.x6.get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if (stick.x5.get()) lower(10); // When button 5 is pressed, lower the winch all the way
		adjust(stick.getZ()); // Always adjust by Z axis
		
		if (winchTimer > 0) winchTimer--; // Winch can move over multiple cycles, so set a countdown
		else winch.set(0); // If the countdown is done, stop the winch
	}

}
