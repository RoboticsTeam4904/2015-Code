package org.usfirst.frc.team4904.robot;

public class AutoAlign implements Updatable{
	
	private final Mecanum mecanum;
	private final UDAR udar;
	private final IMU imu;
	private final LIDAR lidar;
	private final Grabber grabber;
	
	private volatile boolean wide;
	private volatile boolean isCurrentlyAligning;
	public AutoAlign(Mecanum mecanum, UDAR udar, LIDAR lidar, IMU imu, Grabber grabber){
		this.mecanum = mecanum;
		this.udar = udar;
		this.imu = imu;
		this.grabber = grabber;
		this.lidar=lidar;
	}
	
	public void toteGrab(){
		if(isCurrentlyAligning){
			return;
		}
		// TODO put alignment code
		// TODO set "wide" boolean based on sensor data
		if (wide) grabber.setWidth(Operator.MODE_WIDE_TOTE); // if we are picking up a wide tote, set the grabber to the wide tote width
		else grabber.setWidth(Operator.MODE_THIN_TOTE); // if we are picking up a thin tote, set the grabber to the thin tote width. If you are reading the end of this comment, you must be looking for significantly more information.
	}
	
	public void canGrab(){
		if(isCurrentlyAligning){
			return;
		}
		// TODO put alignment code
		
		grabber.setWidth(Operator.MODE_CAN);
	}
	
	public void toteRelease(){
		// TODO put alignment code
		
		if(wide) grabber.setWidth(Operator.MODE_WIDE_TOTE + 10); // set grabber width to wider than tote to release tote
		else grabber.setWidth(Operator.MODE_THIN_TOTE + 10);
	}
	
	public void canRelease(){
		// TODO put alignment code -- why do we need to align when releasing???
		
		grabber.setWidth(Operator.MODE_CAN + 10); // set grabber width to wider than can width to release can
	}

	public void update() {
		if(isCurrentlyAligning){
			// TODO put alignment code
		}
		
	}
	
}
