package org.usfirst.frc.team4904.robot.driver;


import org.usfirst.frc.team4904.robot.AutoAlign;
import org.usfirst.frc.team4904.robot.Driver;
import org.usfirst.frc.team4904.robot.output.Mecanum;

public class AutoDriver extends Driver {
	public AutoDriver(Mecanum mecanumDrive, AutoAlign align) {
		super(mecanumDrive, align);
		// TODO Auto-generated constructor stub
	}
	
	public void disable() {
		mecanumDrive.setDesiredTurnSpeed(0);
		mecanumDrive.setDesiredXYSpeed(0, 0);
	}
	
	public void update() {
		// TODO Auto-generated method stub
	}
}
