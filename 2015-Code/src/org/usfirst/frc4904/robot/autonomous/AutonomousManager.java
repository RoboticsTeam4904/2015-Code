package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.AutoAlign;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.driver.AutoDriver;
import org.usfirst.frc4904.robot.input.Camera;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.operator.AutoOperator;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Mecanum;
import org.usfirst.frc4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousManager {
	private final Autonomous[] autonomouses = new Autonomous[2];
	private static final int YELLOW_TOTE_STACK = 0;
	private static final int LANDFILL_STACK = 1;
	private static final int AUTO_DEFAULT = YELLOW_TOTE_STACK;
	private final Mecanum mecanumDrive;
	private final Winch winch;
	private final Grabber grabber;
	private final AutoAlign align;
	private final LogKitten logger;
	
	public AutonomousManager(Mecanum mecanumDrive, Winch winch, Grabber grabber, AutoAlign align, Camera camera, LIDAR lidar) {
		autonomouses[YELLOW_TOTE_STACK] = new YellowToteStack(camera, align, lidar);
		autonomouses[LANDFILL_STACK] = new LandfillStack(lidar, align);
		this.mecanumDrive = mecanumDrive;
		this.winch = winch;
		this.align = align;
		this.grabber = grabber;
		for (Autonomous auto : autonomouses) {
			registerAutonomous(auto);
		}
		logger = new LogKitten("AutonomousManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putNumber("Autonomous", AUTO_DEFAULT);
	}
	
	public Autonomous getAutonomous() {
		int autoMode = (int) SmartDashboard.getNumber("Autonomous", AUTO_DEFAULT);
		if (autoMode > 0) {
			autoMode = 0;
		}
		logger.v("getAutonomous", "Auto mode " + Integer.toString(autoMode));
		return autonomouses[autoMode];
	}
	
	private void registerAutonomous(Autonomous auto) {
		auto.setAutoDriver(new AutoDriver(mecanumDrive, align, auto));
		auto.setAutoOperator(new AutoOperator(winch, align, grabber, auto));
	}
}
