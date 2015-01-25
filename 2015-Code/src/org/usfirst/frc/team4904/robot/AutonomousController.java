package org.usfirst.frc.team4904.robot;

import org.usfirst.frc.team4904.robot.driver.DriverAutonomous;
import org.usfirst.frc.team4904.robot.input.IMU;
import org.usfirst.frc.team4904.robot.input.LIDAR;
import org.usfirst.frc.team4904.robot.input.UDAR;
import org.usfirst.frc.team4904.robot.operator.OperatorAutonomous;

public class AutonomousController implements Updatable {//This needs to be a seperate class because it cant extend both Operator and Driver
	private OperatorAutonomous operator = null;
	private DriverAutonomous driver = null;
	private int desiredWinchAction = 0;
	private double angle = 0;
	private double speed = 0;
	private double turnSpeed = 0;
	private final UDAR udar;
	private final IMU imu;
	private final LIDAR lidar;

	public AutonomousController(UDAR udar, IMU imu, LIDAR lidar) {
		this.udar = udar;
		this.imu = imu;
		this.lidar = lidar;
	}

	public void setDriver(DriverAutonomous driver) {
		if (this.driver != null) {
			throw new Error("Driver already set");
		}
		this.driver = driver;
	}

	public void setOperator(OperatorAutonomous operator) {
		if (this.operator != null) {
			throw new Error("Operator already set");
		}
		this.operator = operator;
	}

	public synchronized void update() {//This is in this architecture beacuse this 
		//update function might take a long time (e.g. graph search), and we don't want other parts of the robot (e.g. motors) to be slowed by it
		// TODO code all autonomous thing here
		desiredWinchAction++;
		angle++;
		speed++;
		turnSpeed++;
	}

	public int getDesiredWinchAction() {//While these functions might be called very quickly, their return values might change only every once in a while
		return desiredWinchAction;
	}

	public double[] getDesiredMovement() {
		return new double[] { angle, speed, turnSpeed };
	}
}