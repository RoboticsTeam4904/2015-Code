package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.operator.OperatorGriffin;
import org.usfirst.frc.team4904.robot.operator.OperatorNachi;
import org.usfirst.frc.team4904.robot.output.Winch;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class OperatorManager {
	private NetworkTable operatorTable;
	private Operator[] operators;
	public final int OPERATOR_GRIFFIN = 0;
	public final int OPERATOR_NACHI = 1;
	
	public OperatorManager(LogitechJoystick stick, Winch winch, AutoAlign align) {
		operatorTable = NetworkTable.getTable("driverTable");
		this.operators = new Operator[2];
		operators[OPERATOR_GRIFFIN] = new OperatorGriffin(stick, winch, align);
		operators[OPERATOR_NACHI] = new OperatorNachi(stick, winch, align);
	}
	
	public Operator getOperator() {
		int operatorNum = 0;
		operatorTable.retrieveValue("OPERATOR", operatorNum);
		return operators[operatorNum];
	}
}
