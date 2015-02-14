package org.usfirst.frc.team4904.robot.operator;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.output.Winch;

public class OperatorNachi extends Operator {
	private final LogitechJoystick stick;
	private final AutoAlign align;
	
	public OperatorNachi(LogitechJoystick stick, Winch winch, AutoAlign align) {
		super(winch, align);
		this.stick = stick;
		this.align = align;
	}
	
	public synchronized void update() {
		if (stick.buttons[0].get()) { // When button 1 is pressed, toggle tote grabbing
			System.out.print("TOTE");
			if (align.isGrabberEmpty()) {
				System.out.println("Grab");
				grab(MODE_TOTE);
			} else {
				System.out.println("Release");
				release();
			}
		}
		if (stick.buttons[1].get()) { // When button 2 is pressed, toggle can grabbing
			System.out.print("CAN");
			if (align.isGrabberEmpty()) {
				System.out.println("Grab");
				grab(MODE_CAN);
			} else {
				System.out.println("Release");
				release();
			}
		}
		if (stick.buttons[4].get()) {
			changeHeight(1); // When button 5 is pressed, raise the winch one level
		}
		if (stick.buttons[2].get()) {
			changeHeight(-1); // When button 3 is pressed, lower the winch one level
		}
		if (stick.buttons[5].get()) {
			setWinch(12); // When button 6 is pressed, raise the winch all the way
		}
		if (stick.buttons[3].get()) {
			setWinch(-12); // When button 4 is pressed, lower the winch all the way
		}
		if (stick.buttons[10].getRaw()) {
			adjust(stick.getY()); // When button 11 is pressed, adjust the winch
		} else {
			adjust(0);
		}
	}
	
	public void disable() {
		adjust(0);
	}
}