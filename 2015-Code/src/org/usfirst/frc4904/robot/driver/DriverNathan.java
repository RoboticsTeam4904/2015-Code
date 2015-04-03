package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.LogKitten;

public class DriverNathan extends Driver {
	private final LogKitten logger;
	
	public DriverNathan() {
		super("Nathan");
		logger = new LogKitten(LogKitten.LEVEL_ERROR);
	}
	
	public synchronized void update() {
		setMovement(xboxController.leftStick.getX() / 2, xboxController.leftStick.getY() / 2);
		logger.v("" + xboxController.leftStick.getX());
		setTurn(xboxController.rightStick.getX() / 3); // Turns way too fast otherwise
		if (xboxController.back.get()) {
			disable();
			logger.w("Robot killed by driver (Nathan)");
		}
	}
}