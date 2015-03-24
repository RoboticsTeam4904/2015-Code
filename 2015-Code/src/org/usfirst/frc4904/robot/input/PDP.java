package org.usfirst.frc4904.robot.input;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP extends PowerDistributionPanel implements Updatable {
	private static final int NUM_PDP_PORTS = 16;
	private final LogKitten logger;
	private static final boolean logVerbose = false;
	
	public PDP() {
		if (logVerbose) {
			SmartDashboard.putNumber("PDP Temperature: ", 0);
			SmartDashboard.putNumber("PDP Voltage: ", 0);
			for (int i = 0; i < NUM_PDP_PORTS; i++) {
				SmartDashboard.putNumber("PDP Port " + i + " Current", 0);
			}
		}
		logger = new LogKitten();
	}
	
	public void update() {
		if (logVerbose) {
			SmartDashboard.putNumber("PDP Temperature: ", getTemperature());
			logger.v("Temperature: " + Double.toString(getTemperature()));
			SmartDashboard.putNumber("PDP Voltage", getVoltage());
			logger.v("Voltage: " + Double.toString(getVoltage()));
			for (int i = 0; i < NUM_PDP_PORTS; i++) {
				SmartDashboard.putNumber("PDP Port " + i + " Current", getCurrent(i));
				logger.v("Port " + Integer.toString(i) + ": " + Double.toString(getCurrent(i)));
			}
			logger.v("Energy: " + Double.toString(getTotalEnergy()));
			logger.v("Power: " + Double.toString(getTotalPower()));
		}
	}
}
