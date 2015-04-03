package org.usfirst.frc4904.robot.driver;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.TypedNamedSendableChooser;
import org.usfirst.frc4904.robot.input.XboxController;
import org.usfirst.frc4904.robot.output.Mecanum;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverManager extends TypedNamedSendableChooser<Driver> {
	private final LogKitten logger;
	
	public DriverManager(Mecanum mecanum, XboxController xbox) {
		super();
		Driver.passSensors(mecanum, xbox);
		addDefault(new DriverNathan());
		logger = new LogKitten("DriverManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putData("Driver Chooser", this);
	}
	
	public Driver getSelected() {
		return (Driver) super.getSelected();
	}
}