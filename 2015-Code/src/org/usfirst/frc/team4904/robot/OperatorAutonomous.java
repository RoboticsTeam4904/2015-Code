package org.usfirst.frc.team4904.robot;

public class OperatorAutonomous extends Operator{
	AutonomousController controller;
	public OperatorAutonomous(LogitechJoystick stick, Winch winch,
			AutoAlign align, AutonomousController controller) {
		super(stick, winch, align);
		this.controller=controller;
		controller.setOperator(this);
	}

	public void update() {
		// TODO controller.getDesiredWinchAction()
		updateWinch();
	}

}
