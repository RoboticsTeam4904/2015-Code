package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.input.XboxController;

public class DriverNathan extends Driver {
	private final LogKitten logger;
	
	public DriverNathan() {
		super("Nathan");
		logger = new LogKitten();
	}
	
	public synchronized void update() {
		if (!xboxController.connected()) {
			setMovement(0, 0);
			setTurn(0);
			return;
		}
		setMovement(xboxController.getValue(XboxController.X_STICK) / 2, -xboxController.getValue(XboxController.Y_STICK) / 2);
		double turnSpeed = xboxController.getValue(XboxController.TWIST_STICK) / 2; // Turns way too fast otherwise
		setTurn(turnSpeed); // Actually do the turning
		if (xboxController.getButton(XboxController.BACK_BUTTON)) {
			disable();
			logger.w("Robot killed by driver (Nathan)");
		}
		if (xboxController.getButton(XboxController.X_BUTTON)) {
			autoAlign();
		}
		if (xboxController.getButton(XboxController.B_BUTTON)) {
			abortAlign();
		}
	}
}