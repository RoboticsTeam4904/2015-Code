package org.usfirst.frc.team4904.robot.operator;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Autonomous;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.output.Grabber;
import org.usfirst.frc.team4904.robot.output.Winch;

public class AutoOperator extends Operator {
	private final Autonomous auto;
	
	public AutoOperator(Winch winch, AutoAlign align, Grabber grabber, Autonomous auto) {
		super(winch, align, grabber);
		this.auto = auto;
	}
	
	public void update() {
		setWinch(auto.getDesiredWinchHeight());
		auto.setCurrentWinchHeight(getWinch());
	}
}
