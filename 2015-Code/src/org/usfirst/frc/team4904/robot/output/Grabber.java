package org.usfirst.frc.team4904.robot.output;


import org.usfirst.frc.team4904.robot.Operator;
import edu.wpi.first.wpilibj.Talon;

public class Grabber extends Talon {
	private static final double MAX_WIDTH = 100;
	public static final double THIN_TOTE_WIDTH = 0;
	public static final double WIDE_TOTE_WIDTH = 0;
	public static final double CAN_WIDTH = 0;
	
	public Grabber(int channel) {
		super(channel);
	}
	
	private void moveToWidth(double width) {}
	
	public void setWidth(int mode) {
		if (mode == Operator.MODE_EMPTY) {
			moveToWidth(MAX_WIDTH);
		}
		if (mode == Operator.MODE_THIN_TOTE) moveToWidth(THIN_TOTE_WIDTH);
		else if (mode == Operator.MODE_WIDE_TOTE) moveToWidth(WIDE_TOTE_WIDTH);
		else if (mode == Operator.MODE_CAN) moveToWidth(CAN_WIDTH);
		if (mode == Operator.MODE_THIN_TOTE + 10) moveToWidth(THIN_TOTE_WIDTH + 10);
		else if (mode == Operator.MODE_WIDE_TOTE + 10) moveToWidth(WIDE_TOTE_WIDTH + 10);
		else if (mode == Operator.MODE_CAN + 10) moveToWidth(CAN_WIDTH + 10);
	}
	
	public void move(double speed) {
		super.set(speed);
	}
}