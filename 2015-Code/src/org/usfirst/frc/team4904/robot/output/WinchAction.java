package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.AutoAlign;

public abstract class WinchAction {
	public abstract void run(AutoAlign align, Winch winch);
}
