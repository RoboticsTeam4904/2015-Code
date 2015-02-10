package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.driver.DriverNathan;
import org.usfirst.frc.team4904.robot.input.XboxController;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverManager {
	private Driver[] drivers;
	public final int DRIVER_NATHAN = 0;
	
	public DriverManager(Mecanum mecanum, XboxController xbox, AutoAlign align) {
		drivers = new Driver[1];
		drivers[DRIVER_NATHAN] = new DriverNathan(mecanum, xbox, align);
	}
	
	public Driver getDriver() {
		int driverMode = (int) SmartDashboard.getNumber("DB/Slider 0", 0);
		if (driverMode > 0) {
			driverMode = 0;
		}
		return drivers[driverMode];
	}
}