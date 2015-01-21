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
		
		x1 = new JoystickButton(this, 1);
		x2 = new JoystickButton(this, 2);
		x3 = new JoystickButton(this, 3);
		x4 = new JoystickButton(this, 4);
		x5 = new JoystickButton(this, 5);
		x6 = new JoystickButton(this, 6);
		x7 = new JoystickButton(this, 7);
		x8 = new JoystickButton(this, 8);
		x9 = new JoystickButton(this, 9);
		x10 = new JoystickButton(this, 10);
	}
	
	
	
}
