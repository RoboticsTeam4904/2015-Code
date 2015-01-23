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
		case MODE_CAN:
			grab(MODE_CAN);
			break;
		case 4:
			release();
			break;
		}
		if(action<0){
			lower(-action);
		}
		if(action>4){
			raise(action-4);
		}
		updateWinch();
	}

}
