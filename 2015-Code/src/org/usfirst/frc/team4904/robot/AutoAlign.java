package org.usfirst.frc.team4904.robot;

public class AutoAlign {
	
	Mecanum mecanum;
	UDAR udar;
	IMU imu;
	
	Grabber grabber;
	
	public AutoAlign(Mecanum mecanum, UDAR udar, IMU imu, Grabber grabber){
		this.mecanum = mecanum;
		this.udar = udar;
		this.imu = imu;
		this.grabber = grabber;
	}
	
	public void toteGrab(){
		
		boolean wide = false;
		
		if (wide) grabber.setWidth(Operator.MODE_WIDE_TOTE); // if we are picking up a wide tote, set the grabber to the wide tote width
		else grabber.setWidth(Operator.MODE_THIN_TOTE); // if we are picking up a thin tote, set the grabber to the thin tote width. If you are reading the end of this comment, you must be looking for significantly more information.
		}
	
	public void canGrab(){
		
		
		grabber.setWidth(Operator.MODE_CAN);
	}
	
	public void toteRelease(){
		
		
		grabber.setWidth(Operator.MODE_WIDE_TOTE+1); // set 
	}
	
	public void canRelease(){
		
		
		grabber.setWidth(Operator.MODE_CAN+1);
	}
	
}
