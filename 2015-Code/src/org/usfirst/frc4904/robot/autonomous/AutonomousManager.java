package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.driver.AutoDriver;
import org.usfirst.frc4904.robot.input.Camera;
import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.operator.AutoOperator;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Mecanum;
import org.usfirst.frc4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousManager extends SendableChooser {
	private final Mecanum mecanumDrive;
	private final Winch winch;
	private final Grabber grabber;
	private final AutoAlign align;
	private final LogKitten logger;
	
	public AutonomousManager(Mecanum mecanumDrive, Winch winch, Grabber grabber, AutoAlign align, Camera camera, LIDAR lidar, IMU imu) {
		super();
		addObject(new YellowToteStack(camera, grabber, lidar, imu));
		addObject(new LandfillStack(lidar, grabber));
		addObject(new OneToteMove(imu, grabber));
		addDefault(new AutoZoneMove(imu));
		addObject(new Freeze());
		addObject(new TimedBackwardsMove());
		this.mecanumDrive = mecanumDrive;
		this.winch = winch;
		this.align = align;
		this.grabber = grabber;
		logger = new LogKitten("AutonomousManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putData("Autonomous Chooser", this);
	}
	
	public void addObject(Autonomous autonMode) {
		registerAutonomous(autonMode);
		super.addObject(autonMode.getName(), autonMode);
	}
	
	public void addDefault(Autonomous autonMode) {
		registerAutonomous(autonMode);
		super.addDefault(autonMode.getName() + " (default)", autonMode);
	}
	
	public void addObject(String arg0, Object arg1) {
		throw new Error("addObject on AutonomousManager shouldn't be called directly!");
	}
	
	public void addDefault(String arg0, Object arg1) {
		throw new Error("addObject on AutonomousManager shouldn't be called directly!");
	}
	
	public Autonomous getSelected() {
		Autonomous selected = (Autonomous) super.getSelected();
		logger.v("getAutonomous", "Auto mode: " + selected);
		return selected;
	}
	
	private void registerAutonomous(Autonomous autonMode) {
		autonMode.setAutoDriver(new AutoDriver(mecanumDrive, align, autonMode));
		autonMode.setAutoOperator(new AutoOperator(winch, align, grabber, autonMode));
	}
}
