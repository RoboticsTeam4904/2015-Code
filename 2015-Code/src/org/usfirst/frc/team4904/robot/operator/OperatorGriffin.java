package org.usfirst.frc.team4904.robot.operator;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.output.Grabber;
import org.usfirst.frc.team4904.robot.output.Winch;

public class OperatorGriffin extends Operator {
	private final LogitechJoystick stick;
	private final Grabber grabber;
	
	public OperatorGriffin(LogitechJoystick stick, Winch winch, AutoAlign align, Grabber grabber) {
		super(winch, align, grabber);
		this.stick = stick;
		this.grabber = grabber;
	}
	
	public synchronized void update() {
		if (stick.buttons[1].getRaw()) {
			adjust(stick.getY()); // When button 2 is pressed, adjust the winch
		} else {
			adjust(0);
		}
		if (stick.buttons[3].get()) {
			changeHeight(1); // When button 4 is pressed, raise the winch one level
		}
		if (stick.buttons[2].get()) {
			changeHeight(-1); // When button 3 is pressed, lower the winch one level
		}
		if (stick.buttons[5].get()) {
			changeHeight(12); // When button 6 is pressed, raise the winch all the way
		}
		if (stick.buttons[4].get()) {
			changeHeight(-12); // When button 5 is pressed, lower the winch all the way
		}
		if (stick.buttons[11].get()) {
			grab(MODE_TOTE); // When button 12 is pressed, grab a thin tote
		}
		if (stick.buttons[9].get()) {
			grab(MODE_TOTE); // When button 10 is pressed, grab a wide tote
		}
		if (stick.buttons[7].get()) {
			grab(MODE_CAN); // When button 8 is pressed, grab a can
		}
		if (stick.buttons[10].get()) {
			release(); // When button 11 is pressed, release a thin tote
		}
		if (stick.buttons[8].get()) {
			release(); // When button 9 is pressed, release a wide tote
		}
		if (stick.buttons[6].get()) {
			release(); // When button 7 is pressed, release a can
		}
		// Grabber override
		if (stick.buttons[11].getRaw()) {
			grabber.override(stick.getY());
		}
	}
	
	public void disable() {
		adjust(0);
	}
}