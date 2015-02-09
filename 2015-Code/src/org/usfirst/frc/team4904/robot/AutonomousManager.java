package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.autonomous.AutonomousType1;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import org.usfirst.frc.team4904.robot.output.Winch;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class AutonomousManager {
	private NetworkTable autonomousTable;
	private Autonomous[] autonomouses;
	public final int AUTONOMOUS_TYPE_1 = 0;
	
	public AutonomousManager(Mecanum mecanum, Winch winch, AutoAlign align) {
		autonomousTable = NetworkTable.getTable("driverTable");
		this.autonomouses = new Autonomous[1];
		autonomouses[AUTONOMOUS_TYPE_1] = new AutonomousType1(mecanum, winch, align);
	}
	
	public Autonomous getAutonomous() {
		int codeNum = 0;
		autonomousTable.retrieveValue("AUTONOMOUS", codeNum);
		return autonomouses[codeNum];
	}
}
