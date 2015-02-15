package org.usfirst.frc4904.robot;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ModeManager {
	public static final int NO_MODE = 0;
	public static final int TRAIN_PID_MODE = 1;
	public static final int DUMP_LIDAR_MODE = 2;
	public static final int TEST_USB_MODE = 3;
	private static final int DEFAULT_MODE = NO_MODE;
	
	public ModeManager() {
		SmartDashboard.putBoolean("UseMode", false);
		SmartDashboard.putNumber("Mode", DEFAULT_MODE);
	}
	
	public int useMode() {
		if (SmartDashboard.getBoolean("UseMode", false)) {
			return (int) SmartDashboard.getNumber("Mode", DEFAULT_MODE);
		}
		return NO_MODE;
	}
}
