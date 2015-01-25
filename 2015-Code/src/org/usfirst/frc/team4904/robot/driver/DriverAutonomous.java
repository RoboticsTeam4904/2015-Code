package org.usfirst.frc.team4904.robot.driver;

import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.AutonomousController;
import org.usfirst.frc.team4904.robot.Driver;
import org.usfirst.frc.team4904.robot.output.Mecanum;

public class DriverAutonomous extends Driver{
	private final AutonomousController controller;
	public DriverAutonomous(Mecanum mecanumDrive, AutonomousController controller, AutoAlign align) {
		super(mecanumDrive,align);//There is no reason for DriverAutonomous to have access to the XboxController
		this.controller=controller;
		controller.setDriver(this);
	}

	public synchronized void update() {//Once AutonomousController is updated in a different thread than this, we need
		//this architecture in order for it to be thread-safe
		double[] movement=controller.getDesiredMovement();
		setMovement(movement[0],movement[1]);
		setTurn(movement[2]);
	}

}