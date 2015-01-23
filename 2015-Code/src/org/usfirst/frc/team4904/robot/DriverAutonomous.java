package org.usfirst.frc.team4904.robot;

public class DriverAutonomous extends Driver{
	AutonomousController controller;
	public DriverAutonomous(Mecanum mecanumDrive, XboxController xboxController, AutonomousController controller) {
		super(mecanumDrive, xboxController);
		this.controller=controller;
		controller.setDriver(this);
	}

	public void update() {
		// TODO move(controller.getDesiredMovement())
	}

}
