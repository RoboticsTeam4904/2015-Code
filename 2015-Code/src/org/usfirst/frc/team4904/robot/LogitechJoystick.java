package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class LogitechJoystick extends Joystick{
	
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
	Button x11;
	Button x12;
	
	public LogitechJoystick(int port) {
		super(port);
		
		x1 = new JoystickButton(this, 1); // Initilize button 1
		x2 = new JoystickButton(this, 2); // Initilize button 2
		x3 = new JoystickButton(this, 3); // Initilize button 3
		x4 = new JoystickButton(this, 4); // Initilize button 4
		x5 = new JoystickButton(this, 5); // Initilize button 5
		x6 = new JoystickButton(this, 6); // Initilize button 6
		x7 = new JoystickButton(this, 7); // Initilize button 7
		x8 = new JoystickButton(this, 8); // Initilize button 8
		x9 = new JoystickButton(this, 9); // Initilize button 9
		x10 = new JoystickButton(this, 10); // Initilize button 10
		x11 = new JoystickButton(this, 11); // Initilize button 11
		x12 = new JoystickButton(this, 12); // Initilize button 12
	}
	
	
	
}
