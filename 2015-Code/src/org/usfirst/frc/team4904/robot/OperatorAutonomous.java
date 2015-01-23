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
		int action=controller.getDesiredWinchAction();
		switch(action){// TODO finish!!!
		case MODE_THIN_TOTE:
			grab(MODE_THIN_TOTE);
			break;
		case MODE_WIDE_TOTE:
			grab(MODE_WIDE_TOTE);
			break;
		}
		updateWinch();
	}

}
