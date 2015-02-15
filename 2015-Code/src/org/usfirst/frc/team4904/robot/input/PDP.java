package org.usfirst.frc.team4904.robot.input;


import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP extends PowerDistributionPanel implements Updatable {
	private static final int NUM_PDP_PORTS = 16;
	
	public PDP() {
		SmartDashboard.putNumber("PDP Temperature: ", 0);
		SmartDashboard.putNumber("PDP Voltage: ", 0);
		for (int i = 0; i < NUM_PDP_PORTS; i++) {
			SmartDashboard.putNumber("PDP Port " + i + " Current", 0);
		}
	}
	
	public void update() {
		SmartDashboard.putNumber("PDP Temperature: ", getTemperature());
		SmartDashboard.putNumber("PDP Voltage", getVoltage());
		SmartDashboard.putNumber("PDP Current", getTotalCurrent());
		for (int i = 0; i < NUM_PDP_PORTS; i++) {
			SmartDashboard.putNumber("PDP Port " + i + " Current", getCurrent(i));
		}
	}
}
