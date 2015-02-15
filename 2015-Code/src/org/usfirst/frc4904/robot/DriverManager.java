package org.usfirst.frc4904.robot;


import org.usfirst.frc4904.robot.driver.DriverNathan;
import org.usfirst.frc4904.robot.input.XboxController;
import org.usfirst.frc4904.robot.output.Mecanum;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverManager {
	private Driver[] drivers;
	public static final int DRIVER_NATHAN = 0;
	private static final int DRIVER_DEFAULT = DRIVER_NATHAN;
	private final LogKitten logger;
	
	public DriverManager(Mecanum mecanum, XboxController xbox, AutoAlign align) {
		drivers = new Driver[1];
		drivers[DRIVER_NATHAN] = new DriverNathan(mecanum, xbox, align);
		logger = new LogKitten("DriverManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putNumber("Driver", DRIVER_DEFAULT);
	}
	
	public Driver getDriver() {
		int driverMode = (int) SmartDashboard.getNumber("Driver", DRIVER_DEFAULT);
		if (driverMode > 0) {
			driverMode = 0;
		}
		logger.v("getDriver", "Driver mode " + Integer.toString(driverMode));
		return drivers[driverMode];
	}
}