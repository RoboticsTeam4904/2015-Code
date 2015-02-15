package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.Driver;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.input.XboxController;
import org.usfirst.frc4904.robot.output.Mecanum;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

public class DriverNathan extends Driver {
	private final XboxController xboxController;
	private final LogKitten logger;
	
	public DriverNathan(Mecanum mecanumDrive, XboxController xboxController, AutoAlign align) {
		super(mecanumDrive, align);
		this.xboxController = xboxController;
		logger = new LogKitten("DriverNathan", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_WARN);
	}
	
	public synchronized void update() {
		setMovement(-xboxController.getValue(XboxController.Y_STICK) * 2 / Math.PI, xboxController.getValue(XboxController.X_STICK) * 2 / Math.PI);
		// We have to compute the inverse of the fourth derivative of cosine at -1 because the radius of the wheel is a complex number with an i component of 0.
		// This forces us to compensate with multiplying the movement by this factor in order to prevent the sqrt function from overloading during the update of the mecanum.
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
		if (xboxController.getButton(XboxController.BACK_BUTTON)) {
			disable();
			logger.w("update", "disabled");
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