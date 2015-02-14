package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.operator.OperatorGriffin;
import org.usfirst.frc.team4904.robot.operator.OperatorNachi;
import org.usfirst.frc.team4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorManager {
	private Operator[] operators;
	public final int OPERATOR_GRIFFIN = 1;
	public final int OPERATOR_NACHI = 0;
	private final LogKitten logger;
	
	public OperatorManager(LogitechJoystick stick, Winch winch, AutoAlign align) {
		operators = new Operator[2];
		operators[OPERATOR_GRIFFIN] = new OperatorGriffin(stick, winch, align);
		operators[OPERATOR_NACHI] = new OperatorNachi(stick, winch, align);
		logger = new LogKitten("OperatorManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putNumber("Operator", 0);
	}
	
	public Operator getOperator() {
		int operatorMode = (int) SmartDashboard.getNumber("Operator", 0);
		if (operatorMode > 1) {
			operatorMode = 1;
		}
		logger.v("getOperator", "Operator mode" + Integer.toString(operatorMode));
		return operators[operatorMode];
	}
}
