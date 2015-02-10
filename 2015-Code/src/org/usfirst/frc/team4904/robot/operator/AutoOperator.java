package org.usfirst.frc.team4904.robot.operator;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Autonomous;
import org.usfirst.frc.team4904.robot.Operator;
import org.usfirst.frc.team4904.robot.output.Winch;
import org.usfirst.frc.team4904.robot.output.WinchAction;

public class AutoOperator extends Operator {
	private final Autonomous auto;

	public AutoOperator(Winch winch, AutoAlign align, Autonomous auto) {
		super(winch, align);
		this.auto = auto;
	}

	public void update() {
		WinchAction[] actions = auto.getDesiredWinchActions();
		for (WinchAction action : actions) {
			run(action);
		}
	}
}