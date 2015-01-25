package org.usfirst.frc.team4904.robot.operator;

import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.AutonomousController;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.output.Winch;

public class OperatorAutonomous extends Operator{
	private final AutonomousController controller;
	public OperatorAutonomous(Winch winch, AutoAlign align, AutonomousController controller) {
		super(winch, align);
		this.controller=controller;
		controller.setOperator(this);
	}

	public synchronized void update() {
		int action=controller.getDesiredWinchAction();
		switch(action){// TODO improve this (terrible) system or create a WinchAction class/enum.
		case MODE_THIN_TOTE:
			grab(MODE_THIN_TOTE);
			break;
		case MODE_WIDE_TOTE:
			grab(MODE_WIDE_TOTE);
			break;
		case MODE_CAN:
			grab(MODE_CAN);
			break;
		case MODE_EMPTY:
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