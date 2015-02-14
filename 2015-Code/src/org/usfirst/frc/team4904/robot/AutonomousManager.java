package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.autonomous.YellowToteStack;
import org.usfirst.frc.team4904.robot.driver.AutoDriver;
import org.usfirst.frc.team4904.robot.input.Camera;
import org.usfirst.frc.team4904.robot.input.LIDAR;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import org.usfirst.frc.team4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousManager {
	private final Autonomous[] autonomouses = new Autonomous[1];
	public final int AUTONOMOUS_TYPE_1 = 0;
	private final Mecanum mecanumDrive;
	private final Winch winch;
	private final AutoAlign align;
	private final LogKitten logger;
	
	public AutonomousManager(Mecanum mecanumDrive, Winch winch, AutoAlign align, Camera camera, LIDAR lidar) {
		autonomouses[AUTONOMOUS_TYPE_1] = new YellowToteStack(camera, align, lidar);
		this.mecanumDrive = mecanumDrive;
		this.winch = winch;
		this.align = align;
		for (Autonomous auto : autonomouses) {
			registerAutonomous(auto);
		}
		logger = new LogKitten("AutonomousManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putNumber("Autonomous", 0);
	}
	
	public Autonomous getAutonomous() {
		int autoMode = (int) SmartDashboard.getNumber("Autonomous", 0);
		if (autoMode > 0) {
			autoMode = 0;
		}
		logger.v("getAutonomous", "Auto mode " + Integer.toString(autoMode));
		return autonomouses[autoMode];
	}
	
	private void registerAutonomous(Autonomous auto) {
		auto.setAutoDriver(new AutoDriver(mecanumDrive, align, auto));
		auto.setAutoOperator(new AutoOperator(winch, align, auto));
	}
}
