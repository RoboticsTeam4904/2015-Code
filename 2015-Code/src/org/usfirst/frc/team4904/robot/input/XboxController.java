package org.usfirst.frc.team4904.robot.input;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XboxController extends Joystick {
	public final int A_BUTTON = 1;
	public final int B_BUTTON = 2;
	public final int X_BUTTON = 3;
	public final int Y_BUTTON = 4;
	public final int LEFT_BUMPER = 5;
	public final int RIGHT_BUMPER = 6;
	public final int BACK_BUTTON = 7;
	public final int START_BUTTON = 8;
	public final int LEFT_STICK = 9;
	public final int RIGHT_STICK = 10;
	private SuperButton[] buttons = new SuperButton[10];
	private boolean[] pressed = new boolean[10];
	
	public XboxController(int port) {
		super(port);
		buttons[0] = new SuperButton(this, A_BUTTON); // A button
		buttons[1] = new SuperButton(this, B_BUTTON); // B button
		buttons[2] = new SuperButton(this, X_BUTTON); // X button
		buttons[3] = new SuperButton(this, Y_BUTTON); // Y button
		buttons[4] = new SuperButton(this, LEFT_BUMPER); // Left bumper
		buttons[5] = new SuperButton(this, RIGHT_BUMPER); // Right bumper
		buttons[6] = new SuperButton(this, BACK_BUTTON); // Back button
		buttons[7] = new SuperButton(this, START_BUTTON); // Start button
		buttons[8] = new SuperButton(this, LEFT_STICK); // Left stick
		buttons[9] = new SuperButton(this, RIGHT_STICK); // Right stick
		for (int i = 0; i < 10; i++) {
			pressed[i] = false;
		}
	}
}