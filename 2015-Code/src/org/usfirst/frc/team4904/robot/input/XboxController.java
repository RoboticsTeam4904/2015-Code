package org.usfirst.frc.team4904.robot.input;


import edu.wpi.first.wpilibj.Joystick;

public class XboxController extends Joystick {
	public final static int A_BUTTON = 1;
	public final static int B_BUTTON = 2;
	public final static int X_BUTTON = 3;
	public final static int Y_BUTTON = 4;
	public final static int LEFT_BUMPER = 5;
	public final static int RIGHT_BUMPER = 6;
	public final static int BACK_BUTTON = 7;
	public final static int START_BUTTON = 8;
	public final static int LEFT_STICK = 9;
	public final static int RIGHT_STICK = 10;
	public final static int X_STICK = 0; // Left x
	public final static int Y_STICK = 1; // Left y
	public final static int TWIST_STICK = 2; // Button pair on front. Left is twist, trying to determine right
	private SuperButton[] buttons = new SuperButton[10];
	
	public XboxController(int port) {
		super(port);
		for (int i = 0; i < 10; i++) {
			buttons[i] = new SuperButton(this, i + 1);
		}
	}
	
	public double getValue(int axis) {
		double value = 0;
		if (axis == X_STICK) {
			value = this.getX(); // What this is doing should make sense
		} else if (axis == Y_STICK) {
			value = this.getY(); // Ditto above
		} else if (axis == TWIST_STICK) {
			value = this.getRawAxis(5); // Stick mappings here: http://www.chiefdelphi.com/forums/showpost.php?p=1003245&postcount=8
		}
		if (Math.abs(value) < 0.1) { // Xbox does not go perfectly to zero when released
			value = 0.0; // Do it ourselves
		} // Software to solve hardware problems. Maybe not great.
		return value;
	}
}