package org.usfirst.frc.team4904.robot.operator;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.output.Winch;

public class OperatorNachi extends Operator {
	private final LogitechJoystick stick;
	
	public OperatorNachi(LogitechJoystick stick, Winch winch, AutoAlign align) {
		super(winch, align);
		this.stick = stick;
	}
	
	public synchronized void update() {
		// Pressing the button will probably toggle grabbing maybe a dozen times
		// because this function is called many times a second
		// TODO fix this
		if (stick.buttons[0].get()) { // When button 1 is pressed, toggle tote grabbing
			if (isGrabberEmpty()) grab(MODE_WIDE_TOTE);
			else release();
		}
		if (stick.buttons[1].get()) { // When button 2 is pressed, toggle can grabbing
			if (isGrabberEmpty()) grab(MODE_CAN);
			else release();
		}
		if (stick.buttons[3].get()) raise(1); // When button 4 is pressed, raise the winch one level
		if (stick.buttons[2].get()) lower(1); // When button 3 is pressed, lower the winch one level
		if (stick.buttons[5].get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if (stick.buttons[4].get()) lower(10); // When button 5 is pressed, lower the winch all the way
		adjust(stick.getZ()); // Always adjust by Z axis
		updateWinch();
	}
}