package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.LogKitten;

public class DriverNathan extends Driver {
	private final LogKitten logger;
	
	public DriverNathan() {
		super("Nathan");
		logger = new LogKitten();
	}
	
	public synchronized void update() {
		// if (!xboxController.connected()) {
		// setMovement(0, 0);
		// setTurn(0);
		// return;
		// }
		setMovement(xboxController.leftStick.getX() / 2, -xboxController.leftStick.getY() / 2);
		double turnSpeed = xboxController.rightStick.getX() / 2; // Turns way too fast otherwise
		setTurn(turnSpeed); // Actually do the turning
		if (xboxController.back.get()) {
			disable();
			logger.w("Robot killed by driver (Nathan)");
		}
		if (xboxController.x.get()) {
			autoAlign();
		}
		if (xboxController.b.get()) {
			abortAlign();
		}
	}
}