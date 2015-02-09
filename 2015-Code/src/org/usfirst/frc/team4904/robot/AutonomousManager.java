package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.autonomous.AutonomousType1;
import org.usfirst.frc.team4904.robot.driver.AutoDriver;
import org.usfirst.frc.team4904.robot.operator.AutoOperator;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;

public class AutonomousManager {
	private NetworkTable autonomousTable;
	private Autonomous[] autonomouses;
	public final int AUTONOMOUS_TYPE_1 = 0;
	
	public AutonomousManager(AutoDriver driver, AutoOperator operator) {
		autonomousTable = NetworkTable.getTable("driverTable");
		this.autonomouses = new Autonomous[1];
		autonomouses[AUTONOMOUS_TYPE_1] = new AutonomousType1(driver, operator);
	}
	
	public Autonomous getAutonomous() {
		NumberArray values = new NumberArray();
		autonomousTable.retrieveValue("DRIVER", values);
		return autonomouses[(int) values.get(0)];
	}
}
