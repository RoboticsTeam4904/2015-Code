package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.autonomous.AutonomousType1;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;

public class AutonomousManager {
	private NetworkTable autonomousTable;
	private Autonomous[] autonomouses;
	public final int AUTONOMOUS_TYPE_1 = 0;
	
	public AutonomousManager(Mecanum mecanum, AutoOperator operator, AutoAlign align) {
		autonomousTable = NetworkTable.getTable("driverTable");
		this.autonomouses = new Autonomous[1];
		autonomouses[AUTONOMOUS_TYPE_1] = new AutonomousType1(mecanum, operator, align);
	}
	
	public Autonomous getAutonomous() {
		NumberArray values = new NumberArray();
		autonomousTable.retrieveValue("DRIVER", values);
		return autonomouses[(int) values.get(0)];
	}
}
