package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.autonomous.AutonomousType1;
import org.usfirst.frc.team4904.robot.driver.AutoDriver;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import org.usfirst.frc.team4904.robot.output.Winch;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;

public class AutonomousManager {
	private final NetworkTable autonomousTable;
	private final Autonomous[] autonomouses;
	public final int AUTONOMOUS_TYPE_1 = 0;
	private final Mecanum mecanumDrive;
	private final Winch winch;
	private final AutoAlign align;
	
	public AutonomousManager(Mecanum mecanumDrive, Winch winch, AutoAlign align) {
		autonomousTable = NetworkTable.getTable("DB");
		this.autonomouses = new Autonomous[1];
		autonomouses[AUTONOMOUS_TYPE_1] = new AutonomousType1();
		this.mecanumDrive = mecanumDrive;
		this.winch = winch;
		this.align = align;
		for (Autonomous auto : autonomouses) {
			registerAutonomous(auto);
		}
	}
	
	public Autonomous getAutonomous() {
		NumberArray values = new NumberArray();
		autonomousTable.retrieveValue("Slider 3", values);
		return autonomouses[(int) values.get(0)];
	}
	
	private void registerAutonomous(Autonomous auto) {
		auto.setAutoDriver(new AutoDriver(mecanumDrive, align, auto));
		auto.setAutoOperator(new AutoOperator(winch, align, auto));
	}
}
