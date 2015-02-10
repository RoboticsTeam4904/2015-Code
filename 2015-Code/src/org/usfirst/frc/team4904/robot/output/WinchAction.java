package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.AutoAlign;

public class WinchAction {
	private final int amount;
	private final boolean absolute;

	public WinchAction(int amount, boolean absolute) {
		this.amount = amount;
		this.absolute = absolute;
	}

	public void run(AutoAlign align, Winch winch) {
		if (absolute) {
			winch.setHeight(amount);
		} else {
			winch.changeHeight(amount);
		}
	}
}
