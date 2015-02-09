package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.operator.AutoOperator;
import org.usfirst.frc.team4904.robot.output.Mecanum;

public abstract class Autonomous extends Driver {
	private AutoOperator operator;
	
	public Autonomous(Mecanum mecanumDrive, AutoOperator operator, AutoAlign align) {
		super(mecanumDrive, align);
		this.operator = operator;
	}
}
