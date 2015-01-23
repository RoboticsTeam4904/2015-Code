package org.usfirst.frc.team4904.robot;

public class AutoAlign {
	
	private final Mecanum mecanum;
	private final UDAR udar;
	private final IMU imu;
	
	private final Grabber grabber;
	
	private volatile boolean wide;
	
	public AutoAlign(Mecanum mecanum, UDAR udar, IMU imu, Grabber grabber){
		this.mecanum = mecanum;
		this.udar = udar;
		this.imu = imu;
		this.grabber = grabber;
	}
	
	public void toteGrab(){
		
		// TODO put alignment code
		// TODO set "wide" boolean based on sensor data
		if (wide) grabber.setWidth(Operator.MODE_WIDE_TOTE); // if we are picking up a wide tote, set the grabber to the wide tote width
		else grabber.setWidth(Operator.MODE_THIN_TOTE); // if we are picking up a thin tote, set the grabber to the thin tote width. If you are reading the end of this comment, you must be looking for significantly more information.
	}
	
	public void canGrab(){
		// TODO put alignment code
		
		grabber.setWidth(Operator.MODE_CAN);
	}
	
	public void toteRelease(){
		// TODO put alignment code
		
		if(wide) grabber.setWidth(Operator.MODE_WIDE_TOTE + 10); // set grabber width to wider than tote to release tote
		else grabber.setWidth(Operator.MODE_THIN_TOTE + 10);
	}
	
	public void canRelease(){
		// TODO put alignment code
		
		grabber.setWidth(Operator.MODE_CAN + 10); // set grabber width to wider than can width to release can
	}
	
}
