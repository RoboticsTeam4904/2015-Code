package org.usfirst.frc4904.robot.input;


import edu.wpi.first.wpilibj.Joystick;

public class LogitechJoystick extends Joystick {
	public SuperButton[] buttons = new SuperButton[12];
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	private static final double moveThreshold = 0.05;
	
	public LogitechJoystick(int port) {
		super(port);
		for (int i = 0; i < 12; i++) {
			buttons[i] = new SuperButton(this, i + 1); // Initialize all the buttons. Remember the index is one less than the button num
		}
	}
	
	public boolean active(int axis) {
		if (axis == X_AXIS) {
			return super.getX() > moveThreshold;
		} else if (axis == Y_AXIS) {
			return super.getY() > moveThreshold;
		} else {
			return false;
		}
	}
}