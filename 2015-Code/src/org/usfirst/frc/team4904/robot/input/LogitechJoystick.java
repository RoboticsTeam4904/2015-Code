package org.usfirst.frc.team4904.robot.input;


import edu.wpi.first.wpilibj.Joystick;

public class LogitechJoystick extends Joystick {
	public SuperButton[] buttons = new SuperButton[12];

	public LogitechJoystick(int port) {
		super(port);
		for (int i = 0; i < 12; i++) {
			buttons[i] = new SuperButton(this, i + 1); // Initialize all the buttons. Remember the index is one less than the button num
		}
	}
}