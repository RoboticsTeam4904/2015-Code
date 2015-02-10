package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.AutoAlign;

public class GrabRelease extends WinchAction {
	public final boolean grab;
	public final boolean tote;
	
	public GrabRelease(boolean grab, boolean tote) {
		this.grab = grab;
		this.tote = tote;
	}
	
	public void run(AutoAlign align, Winch winch) {
		if (grab) {
			if (tote) {
				align.grabTote();
			} else {
				align.grabCan();
			}
		} else {
			align.release();
		}
	}
}
