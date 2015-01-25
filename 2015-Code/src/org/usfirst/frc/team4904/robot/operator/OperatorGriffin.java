package org.usfirst.frc.team4904.robot.operator;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.output.Winch;

public class OperatorGriffin extends Operator {
	private final LogitechJoystick stick;
	
	public OperatorGriffin(LogitechJoystick stick, Winch winch, AutoAlign align) {
		super(winch, align);
		this.stick = stick;
	}
	
	public synchronized void update() {
		if (stick.x2.get()) adjust(stick.getY()); // When button 2 is pressed, adjust the winch
		if (stick.x4.get()) raise(1); // When button 4 is pressed, raise the winch one level
		if (stick.x3.get()) lower(1); // When button 3 is pressed, lower the winch one level
		if (stick.x6.get()) raise(10); // When button 6 is pressed, raise the winch all the way
		if (stick.x5.get()) lower(10); // When button 5 is pressed, lower the winch all the way
		if (stick.x12.get()) grab(MODE_THIN_TOTE); // When button 12 is pressed, grab a thin tote
		if (stick.x10.get()) grab(MODE_WIDE_TOTE); // When button 10 is pressed, grab a wide tote
		if (stick.x8.get()) grab(MODE_CAN); // When button 8 is pressed, grab a can
		if (stick.x11.get()) release(); // When button 11 is pressed, release a thin tote
		if (stick.x9.get()) release(); // When button 9 is pressed, release a wide tote
		if (stick.x7.get()) release(); // When button 7 is pressed, release a can
		updateWinch();
	}
}