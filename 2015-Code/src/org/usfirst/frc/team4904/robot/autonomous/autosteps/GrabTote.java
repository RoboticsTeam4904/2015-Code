package org.usfirst.frc.team4904.robot.autonomous.autosteps;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Autonomous;
import org.usfirst.frc.team4904.robot.autonomous.Step;

public class GrabTote extends Autonomous implements Step {
	private AutoAlign align;
	
	public GrabTote(AutoAlign align) {
		this.align = align;
	}
	
	public int run() {
		if (align.isGrabberEmpty()) {
			align.grabTote();
		}
		if (align.isCurrentlyAligning()) {
			return 0;
		} else {
			desiredWinchHeight = 1;
		}
		return 1;
	}
}
