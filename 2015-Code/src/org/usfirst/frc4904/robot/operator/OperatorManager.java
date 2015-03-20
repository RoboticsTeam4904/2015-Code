package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.input.LogitechJoystick;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorManager {
	private Operator[] operators;
	public static final int OPERATOR_GRIFFIN = 1;
	public static final int OPERATOR_NACHI = 0;
	private static final int OPERATOR_DEFAULT = OPERATOR_GRIFFIN;
	private final LogKitten logger;
	
	public OperatorManager(LogitechJoystick stick, Winch winch, AutoAlign align, Grabber grabber) {
		operators = new Operator[2];
		operators[OPERATOR_GRIFFIN] = new OperatorGriffin(stick, winch, align, grabber);
		operators[OPERATOR_NACHI] = new OperatorNachi(stick, winch, align, grabber);
		logger = new LogKitten("OperatorManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putNumber("Operator", OPERATOR_DEFAULT);
	}
	
	public Operator getOperator() {
		int operatorMode = (int) SmartDashboard.getNumber("Operator", OPERATOR_DEFAULT);
		if (operatorMode > 1) {
			operatorMode = 1;
		}
		logger.v("getOperator", "Operator mode" + Integer.toString(operatorMode));
		return operators[operatorMode];
	}
}
