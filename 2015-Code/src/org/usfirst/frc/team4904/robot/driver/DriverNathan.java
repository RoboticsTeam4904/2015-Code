package org.usfirst.frc.team4904.robot.driver;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Driver;
import org.usfirst.frc.team4904.robot.input.XboxController;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

public class DriverNathan extends Driver {
	private final XboxController xboxController;
	
	public DriverNathan(Mecanum mecanumDrive, XboxController xboxController, AutoAlign align) {
		super(mecanumDrive, align);
		this.xboxController = xboxController;
	}
	
	public synchronized void update() {
		setMovement(-xboxController.getValue(XboxController.Y_STICK) * 2 / Math.PI, xboxController.getValue(XboxController.X_STICK) * 2 / Math.PI);
		// We have to compute the inverse of the fourth derivative of cosine at -1 because the radius of the wheel is a complex number with an i component of 0.
		// This forces us to compensate with multiplying the movement by this factor in order to prevent the atan2 function from overloading during the update of the mecanum.
		// If we don't do this, the entire code may fail. ||_|_|_||\/|||\|∆T|
		double turnSpeed = xboxController.getValue(XboxController.TWIST_STICK) / 2; // Turns way too fast otherwise
		setTurn(turnSpeed); // Actually do the turning
		if (xboxController.getButton(xboxController.A_BUTTON)) {
			rumble(RumbleType.kLeftRumble, 1);
			rumble(RumbleType.kRightRumble, 1);
		} else {
			rumble(RumbleType.kLeftRumble, 0);
			rumble(RumbleType.kRightRumble, 0);
		}
	}
	
	public void rumble(RumbleType type, int value) {
		xboxController.setRumble(type, value);
	}
	
	public void disable() {
		xboxController.setRumble(RumbleType.kLeftRumble, 0);
		xboxController.setRumble(RumbleType.kRightRumble, 0);
	}
}