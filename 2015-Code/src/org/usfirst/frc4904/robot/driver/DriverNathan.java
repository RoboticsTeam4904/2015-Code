package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.LogKitten;

public class DriverNathan extends Driver {
	private final LogKitten logger;
	private final static double TURN_STICK_DEADZONE = 0.5;
	
	public DriverNathan() {
		super("Nathan");
		logger = new LogKitten(LogKitten.LEVEL_ERROR);
		xboxController.rightStick.setDeadZone(TURN_STICK_DEADZONE);
	}
	
	public synchronized void update() {
		setMovement(xboxController.leftStick.getX() / 2, xboxController.leftStick.getY() / 2);
		logger.v("" + xboxController.leftStick.getX());
		if (xboxController.rightStick.getMagnitude() > TURN_STICK_DEADZONE) {
			setTurn(xboxController.rightStick.getAngle());
		}
		if (xboxController.back.get()) {
			disable();
			logger.w("Robot killed by driver (Nathan)");
		}
	}
}