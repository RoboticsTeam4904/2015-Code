package org.usfirst.frc.team4904.robot.input;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XboxController extends Joystick{
	
	public Button aButton;
	public Button bButton;
	public Button xButton;
	public Button yButton;
	public Button leftBumper;
	public Button rightBumper;
	public Button backButton;
	public Button startButton;
	public Button leftStick;
	public Button rightStick;
	
	public XboxController(int port) {
		super(port);
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
	
	public boolean get(){
		
		return false;
	}
}