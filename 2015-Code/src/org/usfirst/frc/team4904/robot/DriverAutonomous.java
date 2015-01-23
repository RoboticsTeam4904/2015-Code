package org.usfirst.frc.team4904.robot;

public class DriverAutonomous extends Driver{
	AutonomousController controller;
	public DriverAutonomous(Mecanum mecanumDrive, XboxController xboxController, AutonomousController controller, AutoAlign align) {
		super(mecanumDrive, xboxController,align);
		this.controller=controller;
		controller.setDriver(this);
	}

	public void update() {
		double[] movement=controller.getDesiredMovement();
		setMovement(movement[0],movement[1]);
		setTurn(movement[2]);
	}

}
