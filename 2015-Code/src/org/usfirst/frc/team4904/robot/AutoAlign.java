package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class AutoAlign {
	
	Mecanum mecanum;
	UDAR udar;
	IMU imu;
	
	SpeedController grabber;
	
	public AutoAlign(Mecanum mecanum, UDAR udar, IMU imu, SpeedController grabber){
		this.mecanum = mecanum;
		this.udar = udar;
		this.imu = imu;
		this.grabber = grabber;
	}
	
	public void toteGrab(){
		
	}
	
	public void canGrab(){
		
		
	}
	
	public void toteRelease(){
		
	}
	
	public void canRelease(){
		
	}
	
}
