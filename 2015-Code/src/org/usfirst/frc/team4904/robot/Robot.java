package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.input.Camera;
import org.usfirst.frc.team4904.robot.input.IMU;
import org.usfirst.frc.team4904.robot.input.LIDAR;
import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.input.SuperEncoder;
import org.usfirst.frc.team4904.robot.input.UDAR;
import org.usfirst.frc.team4904.robot.input.XboxController;
import org.usfirst.frc.team4904.robot.output.DampenedMotor;
import org.usfirst.frc.team4904.robot.output.Grabber;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import org.usfirst.frc.team4904.robot.output.Winch;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	// Default ports for joystick/controller
	private static final int JOYSTICK_PORT = 0;
	private static final int CONTROLLER_PORT = 1;
	// Default ports for motors
	private static final int FRONT_LEFT_WHEEL_PORT = 0;
	private static final int FRONT_RIGHT_WHEEL_PORT = 1;
	private static final int BACK_LEFT_WHEEL_PORT = 2;
	private static final int BACK_RIGHT_WHEEL_PORT = 3;
	private static final int WINCH_PORT = 4;
	private static final int GRABBER_PORT = 5;
	private static final int LIDAR_MOTOR_PORT = 6;
	// Default ports for limit switches
	private static final int RIGHT_INNER_SWITCH_PORT = 0;
	private static final int LEFT_INNER_SWITCH_PORT = 1;
	private static final int RIGHT_OUTER_SWITCH_PORT = 2;
	private static final int LEFT_OUTER_SWITCH_PORT = 3;
	private static final int FRONT_LEFT_I2C_PORT = 10;
	private static final int FRONT_RIGHT_I2C_PORT = 11;
	private static final int BACK_LEFT_I2C_PORT = 12;
	private static final int BACK_RIGHT_I2C_PORT = 13;
	private static final int WINCH_I2C_PORT = 14;
	private static final double MOTOR_P_COEFFICIENT = 0.1;
	private static final double MOTOR_I_COEFFICIENT = 0.1;
	private static final double MOTOR_D_COEFFICIENT = 0.1;
	private final LogitechJoystick stick; // the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private final XboxController xboxController; // the Xbox 360 controller - driver
	// Input devices
	private final UDAR udar; // the UDAR (ultrasonic detection and ranging)
	private final IMU imu;
	private final LIDAR lidar;
	private final DigitalInput limitSwitches[] = new DigitalInput[4];
	private final Camera camera;
	private final SuperEncoder frontLeftEncoder;
	private final SuperEncoder frontRightEncoder;
	private final SuperEncoder backLeftEncoder;
	private final SuperEncoder backRightEncoder;
	private final SuperEncoder winchEncoder;
	// movement controllers
	private final Winch winch; // the Winch class takes care of moving to specific heights
	private final Grabber grabber; // the grabber class takes care of opening and closing the grabber
	private final Mecanum mecanumDrive; // the Mecanum class that takes care of the math required to use mecanum drive
	private final DampenedMotor frontLeftWheel;
	private final DampenedMotor frontRightWheel;
	private final DampenedMotor backLeftWheel;
	private final DampenedMotor backRightWheel;
	// Managers
	private final DriverManager driverManager;
	private final OperatorManager operatorManager;
	private final AutonomousManager autonomousManager;
	// Drivers and operators
	private Driver driver;
	private Operator operator;
	private Autonomous autonomous;
	private final AutoAlign align; // the AutoAlign class contains code to align the robot with totes and cans
	// Update system
	public static final double fastUpdatePeriod = 0.015; // update every 0.005 seconds/5 milliseconds (200Hz)
	public static final double slowUpdatePeriod = 0.2; // update every 0.2 seconds/200 milliseconds (5Hz)
	// Logging system
	private final LogKitten logger;
	private final Disablable[] toDisable;
	// Power distribution board
	private final PowerDistributionPanel pdp;
	
	private enum RobotState {
		DISABLED, OPERATOR, AUTONOMOUS
	}
	
	public Robot() {
		System.out.println("*** CONSTRUCTING ROBOT ***");
		// Initializing logging
		logger = new LogKitten("Robot", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_VERBOSE);// TODO Since this level is less than level_fatal, fatal errors will not be logged
		logger.v("Constructing", "Constructing");
		// Initialize sensors
		imu = new IMU(); // Initialize IMU
		udar = new UDAR(); // Initialize UDAR
		lidar = new LIDAR(LIDAR_MOTOR_PORT); // Initialize LIDAR
		limitSwitches[Grabber.RIGHT_INNER_SWITCH] = new DigitalInput(RIGHT_INNER_SWITCH_PORT);
		limitSwitches[Grabber.LEFT_INNER_SWITCH] = new DigitalInput(LEFT_INNER_SWITCH_PORT);
		limitSwitches[Grabber.RIGHT_OUTER_SWITCH] = new DigitalInput(RIGHT_OUTER_SWITCH_PORT);
		limitSwitches[Grabber.LEFT_OUTER_SWITCH] = new DigitalInput(LEFT_OUTER_SWITCH_PORT);
		camera = new Camera();
		// Initialize Encoders
		frontLeftEncoder = new SuperEncoder(FRONT_LEFT_I2C_PORT);
		frontRightEncoder = new SuperEncoder(FRONT_RIGHT_I2C_PORT);
		backLeftEncoder = new SuperEncoder(BACK_LEFT_I2C_PORT);
		backRightEncoder = new SuperEncoder(BACK_RIGHT_I2C_PORT);
		winchEncoder = new SuperEncoder(WINCH_I2C_PORT);
		// Initialize movement controllers
		winch = new Winch(WINCH_PORT, winchEncoder); // Initialize Winch control
		grabber = new Grabber(GRABBER_PORT, limitSwitches); // Initialize Grabber control -- only autoalign has access to this, by design
		// Initialize motor controllers with default ports
		frontLeftWheel = new DampenedMotor(FRONT_LEFT_WHEEL_PORT);
		frontRightWheel = new DampenedMotor(FRONT_RIGHT_WHEEL_PORT);
		backLeftWheel = new DampenedMotor(BACK_LEFT_WHEEL_PORT);
		backRightWheel = new DampenedMotor(BACK_RIGHT_WHEEL_PORT);
		mecanumDrive = new Mecanum(frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, imu); // Initialize Mecanum control
		// Initialize joysticks (numbers correspond to value set by driver station)
		stick = new LogitechJoystick(JOYSTICK_PORT);
		xboxController = new XboxController(CONTROLLER_PORT);
		// Initalize subsystems
		align = new AutoAlign(mecanumDrive, udar, lidar, imu, grabber, winch); // Initialize AutoAlign system
		// Initialize managers
		driverManager = new DriverManager(mecanumDrive, xboxController, align);
		operatorManager = new OperatorManager(stick, winch, align, grabber);
		autonomousManager = new AutonomousManager(mecanumDrive, winch, grabber, align, camera, lidar);
		// Drivers, operators, autonomous
		autonomous = autonomousManager.getAutonomous();
		toDisable = new Disablable[] {winch, grabber, lidar, driver, operator, autonomous, frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, camera};
		pdp = new PowerDistributionPanel();
	}
	
	public void robotInit() {
		System.out.println("*** INITIALIZING ***");
		logger.v("Initializing", "Initializing");
		SmartDashboard.putBoolean("TrainPID", false);
	}
	
	public void disabled() {
		System.out.println("*** DISABLED ***");
		logger.v("Disabled", "Disabled");
		RobotState state = RobotState.DISABLED;
		new Updater(state, new Updatable[] {imu}, fastUpdatePeriod).start(); // These should have fast updates
		while (isDisabled()) {
			for (Disablable implementsdisable : toDisable) {
				if (implementsdisable != null) {
					implementsdisable.disable();
				}
			}
			frontLeftWheel.set(0);
			frontRightWheel.set(0);
			backLeftWheel.set(0);
			backRightWheel.set(0);
			SmartDashboard.putNumber("Temperature", pdp.getTemperature());
			SmartDashboard.putNumber("Voltage", pdp.getVoltage());
			SmartDashboard.putNumber("Current", pdp.getCurrent(1));
			// System.out.println("Current " + pdp.getCurrent(1));
			Timer.delay(0.01);
		}
	}
	
	public void autonomous() {
		System.out.println("*** AUTONOMOUS ***");
		logger.v("Autonomous", "Autonomous");
		RobotState state = RobotState.AUTONOMOUS;
		autonomous = autonomousManager.getAutonomous();
		driver = autonomous.getAutoDriver();
		operator = autonomous.getAutoOperator();
		new Updater(state, new Updatable[] {align, autonomous, camera}, slowUpdatePeriod).start(); // Controller and align are potentially slower
		new Updater(state, new Updatable[] {imu, driver, operator, mecanumDrive, lidar}, fastUpdatePeriod).start(); // These should have fast updates
		new Updater(state, new Updatable[] {frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, grabber, winch}, fastUpdatePeriod).start();
		while (getRobotState() == state) {
			Timer.delay(0.01);
		}
	}
	
	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***");
		logger.v("Teleoperated", "Teleoperated");
		RobotState state = RobotState.OPERATOR;
		if (SmartDashboard.getBoolean("TrainPID")) {
			trainPID(state);
			return;
		}
		operator = operatorManager.getOperator();
		driver = driverManager.getDriver();
		new Updater(state, new Updatable[] {align, camera}, slowUpdatePeriod).start(); // Controller and align are potentially slower
		new Updater(state, new Updatable[] {imu, driver, operator, mecanumDrive, lidar}, fastUpdatePeriod).start(); // These should have fast updates
		new Updater(state, new Updatable[] {frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, grabber, winch}, fastUpdatePeriod).start();
		while (getRobotState() == state) {
			Timer.delay(0.01);
			SmartDashboard.putNumber("Temperature", pdp.getTemperature());
			SmartDashboard.putNumber("Voltage", pdp.getVoltage());
			SmartDashboard.putNumber("Current", pdp.getCurrent(1));
			System.out.println("Current " + pdp.getCurrent(1));
		}
	}
	
	public void trainPID(RobotState state) {
		System.out.println("*** TEST ***");
		logger.v("Test", "Test");
		new Updater(state, new Updatable[] {frontLeftWheel, mecanumDrive}, fastUpdatePeriod).start();
		while (getRobotState() == state) {
			frontLeftWheel.setValue(0.1);
			logger.v("trainPID", "training cycle");
		}
	}
	
	private RobotState getRobotState() {
		if (isDisabled()) {
			return RobotState.DISABLED;
		}
		if (isOperatorControl()) {
			return RobotState.OPERATOR;
		}
		if (isAutonomous()) {
			return RobotState.AUTONOMOUS;
		}
		return RobotState.DISABLED;
	}
	
	public static double time() {
		return (double) System.currentTimeMillis() / 1000;
	}
	
	private class Updater extends Thread { // Function to update automatically in a new thread
		private final RobotState robotState;
		private final Updatable[] toUpdate;
		private final double updateSpeed;
		
		public Updater(RobotState state, Updatable[] toUpdate, double updateSpeed) {
			robotState = state;
			this.toUpdate = toUpdate;
			this.updateSpeed = updateSpeed;
		}
		
		public void run() {
			if (toUpdate.length > 1) {
				for (Updatable u : toUpdate) {
					new Updater(robotState, new Updatable[] {u}, updateSpeed).start();
				}
				return;
			}
			double desiredTime = time() + updateSpeed; // Sync with clock to ensure that update interval is consistent regardless of how long each update takes
			System.out.println("Starting");
			while (getRobotState() == robotState) {
				for (Updatable update : toUpdate) {
					update.update();
				}
				double delay = desiredTime - time();
				if (delay > 0) {
					Timer.delay(delay); // Wait until the time that this tick should end
				} else {
					if (delay < -0.1) {
						logger.d("Run", "Delay is " + delay + " seconds for " + updateSpeed);
					}
				}
				desiredTime += updateSpeed; // Next tick should end updatePeriod seconds in the future
			}
			System.out.println("Terminating");
			logger.v("Run", "Terminating");
		}
	}
}