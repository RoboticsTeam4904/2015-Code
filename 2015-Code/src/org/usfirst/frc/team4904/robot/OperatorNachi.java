package org.usfirst.frc.team4904.robot;


public class OperatorNachi extends Operator {
	private final LogitechJoystick stick;
	
	public OperatorNachi(LogitechJoystick stick, Winch winch, AutoAlign align){
		super(winch, align);
		this.stick=stick;
	}
	
	public synchronized void update() {
		// Pressing the button will probably toggle grabbing maybe a dozen times
		// because this function is called many times a second
		// TODO fix this
		if (stick.x1.get()){ // When button 1 is pressed, toggle tote grabbing
			if(isGrabberEmpty()) grab(MODE_WIDE_TOTE);
			else release();
		}
		if (stick.x2.get()){ // When button 2 is pressed, toggle can grabbing
			if(isGrabberEmpty()) grab(MODE_CAN);
			else release();
		}
		if (stick.x4.get()) raise(1); // When button 4 is pressed, raise the winch one level
		if (stick.x3.get()) lower(1); // When button 3 is pressed, lower the winch one level
		if (stick.x6.get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if (stick.x5.get()) lower(10); // When button 5 is pressed, lower the winch all the way
		adjust(stick.getZ()); // Always adjust by Z axis
		
		updateWinch();
	}

}
