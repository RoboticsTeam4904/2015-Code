package org.usfirst.frc4904.robot;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class Safeguard {
	private final String smartDashboardName;
	private final boolean smartDashboardEnablable;
	
	public Safeguard(String smartDashboardName) {
		this.smartDashboardEnablable = true;
		this.smartDashboardName = smartDashboardName;
		SmartDashboard.putBoolean(smartDashboardName, true);
	}
	
	public Safeguard() {
		this.smartDashboardEnablable = false;
		this.smartDashboardName = null;
	}
	
	public boolean isSafe() {
		if (smartDashboardEnablable) {
			if (SmartDashboard.getBoolean(smartDashboardName, true)) {
				return safetyCondition();
			}
		}
		return true; // When safeguard is disabled it should assume safety (should not fire)
	}
	
	protected abstract boolean safetyCondition();
}
