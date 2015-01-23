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
		
		if (wide) grabber.setWidth(grabber.MODE_WIDE_TOTE);
		else grabber.setWidth(grabber.MODE_THIN_TOTE);
		}
	
	public void canGrab(){
		
		
		grabber.setWidth(grabber.MODE_CAN);
	}
	
	public void toteRelease(){
		
		
		grabber.setWidth(grabber.MODE_WIDE_TOTE+1);
	}
	
	public void canRelease(){
		
	}
	
}
