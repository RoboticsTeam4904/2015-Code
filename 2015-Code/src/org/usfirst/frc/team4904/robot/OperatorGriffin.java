package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class OperatorGriffin extends Operator {
	
	public OperatorGriffin(LogitechJoystick stick, SpeedController winch, AutoAlign align){
		super(stick, winch, align);
	}
	
	public void update() {
		if (stick.x2.get()) adjust(stick.getY()); // When button 2 is pressed, adjust the winch
		if (stick.x4.get()) raise(1); // When button 4 is pressed, raise the winch one level
		if (stick.x3.get()) lower(1); // When button 3 is pressed, lower the winch one level
		if (stick.x6.get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if (stick.x5.get()) lower(10); // When button 5 is pressed, lower the winch all the way
		if (stick.x12.get()) grab(MODE_THIN_TOTE); // When button 12 is pressed, grab a thin tote
		if (stick.x10.get()) grab(MODE_WIDE_TOTE); // When button 10 is pressed, grab a wide tote
		if (stick.x8.get()) grab(MODE_CAN); // When button 8 is pressed, grab a can
		if (stick.x11.get()) release(MODE_THIN_TOTE); // When button 11 is pressed, release a thin tote
		if (stick.x9.get()) release(MODE_WIDE_TOTE); // When button 9 is pressed, release a wide tote
		if (stick.x7.get()) release(MODE_CAN); // When button 7 is pressed, release a can
	 	
		if (winchTimer > 0) winchTimer--; // Winch can move over multiple cycles, so set a countdown
		else winch.set(0); // If the countdown is done, stop the winch
	}

}
