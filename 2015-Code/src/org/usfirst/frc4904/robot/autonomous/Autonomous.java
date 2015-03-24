package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.Disablable;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Named;
import org.usfirst.frc4904.robot.Updatable;
import org.usfirst.frc4904.robot.driver.AutoDriver;
import org.usfirst.frc4904.robot.input.Camera;
import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.operator.AutoOperator;
import org.usfirst.frc4904.robot.output.Grabber;

public class Autonomous implements Updatable, Disablable, Named {
	private AutoOperator operator = null;
	private AutoDriver driver = null;
	protected volatile double desiredXMovement = 0;
	protected volatile double desiredYMovement = 0;
	protected volatile double desiredTurnSpeed = 0;
	protected volatile double desiredWinchHeight = 0;
	protected volatile double currentWinchHeight = 0;
	protected static Camera camera;
	protected static Grabber grabber;
	protected static LIDAR lidar;
	protected static IMU imu;
	protected final Step[] steps;
	private boolean finished = false;
	private boolean firstInit = false;
	private final String name;
	private int currentStep = 0;
	private final LogKitten logger;
	
	protected Autonomous(String name, Step[] steps) {
		this.steps = steps;
		logger = new LogKitten(LogKitten.LEVEL_DEBUG);
		this.name = name;
	}
	
	public static void passSensors(Camera camera, Grabber grabber, LIDAR lidar, IMU imu) {
		Autonomous.camera = camera;
		Autonomous.grabber = grabber;
		Autonomous.lidar = lidar;
		Autonomous.imu = imu;
	}
	
	public AutoDriver getAutoDriver() {
		return driver;
	}
	
	public String getName() {
		return name;
	}
	
	public AutoOperator getAutoOperator() {
		return operator;
	}
	
	public void setAutoOperator(AutoOperator operator) {
		if (this.operator != null) {
			throw new Error("Operator already set");
		}
		this.operator = operator;
	}
	
	public void setAutoDriver(AutoDriver driver) {
		if (this.driver != null) {
			throw new Error("Driver already set");
		}
		this.driver = driver;
	}
	
	public void update() {
		if (finished) {
			resetMovement();
			return;
		}
		if (!firstInit) {
			firstInit = true;
			steps[0].init();
		}
		Step step = steps[currentStep];
		step.setCurrentWinchHeight(currentWinchHeight);
		boolean stepCompleted = step.run();
		double[] movement = step.getDesiredMovement();
		this.desiredTurnSpeed = movement[2];
		this.desiredWinchHeight = step.getDesiredWinchHeight();
		this.desiredXMovement = movement[0];
		this.desiredYMovement = movement[1];
		if (stepCompleted) {
			logger.d("autonomous step " + currentStep + " completed");
			currentStep++;
			if (currentStep >= steps.length) {// Otherwise, this would throw an ArrayIndexOutOfBoundsException when the last step finished
				finished = true;
				resetMovement();
				return;
			}
			steps[currentStep].init();
			resetMovement(); // A step might leave the desiredMovement variables in a nonzero state
		}
	}
	
	public double[] getDesiredMovement() {
		return new double[] {desiredXMovement, desiredYMovement, desiredTurnSpeed};
	}
	
	public double getDesiredWinchHeight() {
		return desiredWinchHeight;
	}
	
	public void setCurrentWinchHeight(double currentWinchHeight) {
		this.currentWinchHeight = currentWinchHeight;
	}
	
	private void resetMovement() {
		desiredXMovement = 0;
		desiredYMovement = 0;
		desiredTurnSpeed = 0;
		desiredWinchHeight = currentWinchHeight;
	}
	
	public void disable() {
		resetMovement();
		firstInit = false;
		currentStep = 0;
		finished = false;
	}
}
