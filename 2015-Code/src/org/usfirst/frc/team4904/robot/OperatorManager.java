package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.operator.OperatorGriffin;
import org.usfirst.frc.team4904.robot.operator.OperatorNachi;
import org.usfirst.frc.team4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorManager {
	private Operator[] operators;
	public final int OPERATOR_GRIFFIN = 0;
	public final int OPERATOR_NACHI = 1;
	private final LogKitten logger;
	
	public OperatorManager(LogitechJoystick stick, Winch winch, AutoAlign align) {
		operators = new Operator[2];
		operators[OPERATOR_GRIFFIN] = new OperatorGriffin(stick, winch, align);
		operators[OPERATOR_NACHI] = new OperatorNachi(stick, winch, align);
		logger = new LogKitten("OperatorManager", LogKitten.LEVEL_VERBOSE);
	}
	
	public Operator getOperator() {
		int operatorMode = (int) SmartDashboard.getNumber("DB/Slider 1", 0);
		if (operatorMode > 1) {
			operatorMode = 1;
		}
		logger.v("getOperator", "Operator mode" + Integer.toString(operatorMode));
		return operators[operatorMode];
	}
}
