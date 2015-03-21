package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.autonomous.Autonomous;

public class AutoOperator extends Operator {
	private final Autonomous auto;
	
	public AutoOperator(Autonomous auto) {
		super("AutoOperator");
		this.auto = auto;
	}
	
	public void update() {
		setWinchHeight(auto.getDesiredWinchHeight());
		auto.setCurrentWinchHeight(getWinch());
	}
}
