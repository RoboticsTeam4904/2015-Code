package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.Talon;

public class Grabber extends Talon {
	
	static final double MAX_WIDTH = 100;

	static final int MODE_THIN_TOTE = 0;
	static final int MODE_WIDE_TOTE = 1;
	static final int MODE_CAN = 2;

	private static final double THIN_TOTE_WIDTH = 0;
	private static final double WIDE_TOTE_WIDTH = 0;
	private static final double CAN_WIDTH = 0;

	public Grabber(int channel) {
		super(channel);
	}
	
	private void moveToWidth(double width){
		
	}
	
	public void setWidth(int mode){
		if (mode == MODE_THIN_TOTE) moveToWidth(THIN_TOTE_WIDTH);
		else if (mode == MODE_WIDE_TOTE) moveToWidth(WIDE_TOTE_WIDTH);
		else if (mode == MODE_CAN) moveToWidth(CAN_WIDTH);
	}
	
	public void move(double speed){
		super.set(speed);
	}

}
