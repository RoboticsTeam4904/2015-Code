package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.driver.DriverNathan;
import org.usfirst.frc.team4904.robot.input.XboxController;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;

public class DriverManager {
	private NetworkTable driverTable;
	private Driver[] drivers;
	public final int DRIVER_NATHAN = 0;
	
	public DriverManager(Mecanum mecanum, XboxController xbox, AutoAlign align) {
		driverTable = NetworkTable.getTable("DB");
		this.drivers = new Driver[1];
		drivers[DRIVER_NATHAN] = new DriverNathan(mecanum, xbox, align);
	}
	
	public Driver getDriver() {
		NumberArray values = new NumberArray();
		driverTable.retrieveValue("Button 1", values);
		return drivers[(int) values.get(0)];
	}
}