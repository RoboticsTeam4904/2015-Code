package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.LogKitten;

public class DriverNathan extends Driver {
	private final LogKitten logger;
	private final static double TURN_STICK_DEADZONE = 0.1;
	private double angle = 0;
	
	public DriverNathan() {
		super("Nathan");
		logger = new LogKitten();
		xboxController.rightStick.setDeadZone(TURN_STICK_DEADZONE);
	}
	
	public synchronized void update() {
		setMovement(xboxController.leftStick.getX(), xboxController.leftStick.getY());
		logger.v("" + xboxController.leftStick.getX());
		setTurn(xboxController.rightStick.getX() / 3.0); // Turns way too fast otherwise
		// if (xboxController.rightStick.getMagnitude() > TURN_STICK_DEADZONE) {
		angle += xboxController.rightStick.getX() * 6;
		angle = ((angle % 360) + 360) % 360;
		System.out.println("target angle " + angle + " joystick input: " + xboxController.rightStick.getX());
		setAngle(angle);
		// }
		if (xboxController.y.get()) {
			disable();
			logger.w("Robot killed by driver (Nathan)");
		}
		if (xboxController.a.get()) {
			zeroMecanum();
		}
	}
}