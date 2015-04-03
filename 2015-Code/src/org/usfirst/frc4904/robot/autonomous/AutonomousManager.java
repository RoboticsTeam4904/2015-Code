package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.TypedNamedSendableChooser;
import org.usfirst.frc4904.robot.driver.AutoDriver;
import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.operator.AutoOperator;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousManager extends TypedNamedSendableChooser<Autonomous> {
	private final LogKitten logger;
	
	public AutonomousManager(Winch winch, Grabber grabber, LIDAR lidar, IMU imu) {
		super();
		Autonomous.passSensors(grabber, lidar, imu);
		addObject(new YellowToteStack());
		addObject(new LandfillStack());
		addObject(new OneToteMove());
		addDefault(new AutoZoneMove());
		addObject(new Freeze());
		addObject(new TimedBackwardsMove());
		logger = new LogKitten();
		SmartDashboard.putData("Autonomous Chooser", this);
	}
	
	public void addObject(Autonomous autonMode) {
		registerAutonomous(autonMode);
		super.addObject(autonMode);
	}
	
	public void addDefault(Autonomous autonMode) {
		registerAutonomous(autonMode);
		super.addDefault(autonMode);
	}
	
	private void registerAutonomous(Autonomous autonMode) {
		autonMode.setAutoDriver(new AutoDriver(autonMode));
		autonMode.setAutoOperator(new AutoOperator(autonMode));
	}
}
