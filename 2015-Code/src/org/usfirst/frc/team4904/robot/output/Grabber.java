package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Grabber extends Talon implements Updatable {
	public static final int RIGHT_INNER_SWITCH = 0;
	public static final int LEFT_INNER_SWITCH = 1;
	public static final int RIGHT_OUTER_SWITCH = 2;
	public static final int LEFT_OUTER_SWITCH = 3;
	DigitalInput[] limitSwitches = new DigitalInput[4];
	
	public enum GrabberState {
		OPEN, CLOSED
	}
	GrabberState grabberState;
	
	public Grabber(int channel, DigitalInput[] limitSwitches) {
		super(channel);
		this.limitSwitches = limitSwitches;
		grabberState = GrabberState.OPEN;
	}
	
	public void set(GrabberState state) {
		grabberState = state;
	}
	
	public void move(double speed) {
		super.set(speed);
	}
	
	public void update() {
		switch (grabberState) {
			case OPEN:
				move(0.5);
				return;
			case CLOSED:
				move(-0.5);
				return;
			default:
				return;
		}
	}
}