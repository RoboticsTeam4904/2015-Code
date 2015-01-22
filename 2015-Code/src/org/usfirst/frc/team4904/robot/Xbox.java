package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Xbox extends Joystick{
	
	Button x1;
	Button x2;
	Button x3;
	Button x4;
	Button x5;
	Button x6;
	Button x7;
	Button x8;
	Button x9;
	Button x10;
	
	public Xbox(int port) {
		super(port);
		// TODO Auto-generated constructor stub
		
		aButton = new JoystickButton(this, 1); // A button
		bButton = new JoystickButton(this, 2); // B button
		xButton = new JoystickButton(this, 3); // X button
		yButton = new JoystickButton(this, 4); // Y button
		leftBumper = new JoystickButton(this, 5); // Left bumper
		rightBumper = new JoystickButton(this, 6); // Right bumper
		backButton = new JoystickButton(this, 7); // Back button
		startButton = new JoystickButton(this, 8); // Start button
		leftStick = new JoystickButton(this, 9); // Left stick
		rightStick = new JoystickButton(this, 10); // Right stick
	}
	
	
	
}
