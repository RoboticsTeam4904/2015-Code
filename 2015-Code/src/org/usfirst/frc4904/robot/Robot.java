package org.usfirst.frc4904.robot;


import org.usfirst.frc4904.robot.autonomous.Autonomous;
import org.usfirst.frc4904.robot.autonomous.AutonomousManager;
import org.usfirst.frc4904.robot.driver.Driver;
import org.usfirst.frc4904.robot.driver.DriverManager;
import org.usfirst.frc4904.robot.input.Camera;
import org.usfirst.frc4904.robot.input.IMU;
import org.usfirst.frc4904.robot.input.LIDAR;
import org.usfirst.frc4904.robot.input.LogitechJoystick;
import org.usfirst.frc4904.robot.input.PDP;
import org.usfirst.frc4904.robot.input.SuperEncoder;
import org.usfirst.frc4904.robot.input.SuperSerial;
import org.usfirst.frc4904.robot.input.UDAR;
import org.usfirst.frc4904.robot.input.XboxController;
import org.usfirst.frc4904.robot.operator.Operator;
import org.usfirst.frc4904.robot.operator.OperatorManager;
import org.usfirst.frc4904.robot.output.DampenedMotor;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Mecanum;
import org.usfirst.frc4904.robot.output.Winch;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

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
	// Default ports for limit switches
	private static final int RIGHT_INNER_SWITCH_PORT = 0;
	private static final int LEFT_INNER_SWITCH_PORT = 1;
	private static final int RIGHT_OUTER_SWITCH_PORT = 2;
	private static final int LEFT_OUTER_SWITCH_PORT = 3;
	// Various I2C ports
	private static final int FRONT_LEFT_ENCODER_I2C_PORT = 10;
	private static final int FRONT_RIGHT_ENCODER_I2C_PORT = 11;
	private static final int BACK_LEFT_ENCODER_I2C_PORT = 12;
	private static final int BACK_RIGHT_ENCODER_I2C_PORT = 13;
	private static final int WINCH_ENCODER_I2C_PORT = 14;
	private static final int UDAR_I2C_PORT = 4;
	private static final double WINCH_P_COEFFICIENT = 0.1;
	private static final double WINCH_I_COEFFICIENT = 0.1;
	private static final double WINCH_D_COEFFICIENT = 0.1;
	private static final double MECANUM_P_COEFFICIENT = 0.1;
	private static final double MECANUM_I_COEFFICIENT = 0.1;
	private static final double MECANUM_D_COEFFICIENT = 0.1;
	private final LogitechJoystick stick; // the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private final XboxController xboxController; // the Xbox 360 controller - driver
	// Input devices
	private final SuperSerial serial;
	private final UDAR udar; // the UDAR (ultrasonic detection and ranging)
	private final IMU imu;
	private final LIDAR lidar;
	private final DigitalInput limitSwitches[] = new DigitalInput[4];
	private final Camera camera;
	// private final DemoLightSequence lightSequence;
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
	private final ModeManager modeManager;
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
	// Disables
	private final Disablable[] toDisable;
	private final Updatable[] alwaysUpdate;
	private final Updatable[] alwaysUpdateSlow;
	// Power distribution board
	private final PDP pdp;
	
	private enum RobotState {
		DISABLED, OPERATOR, AUTONOMOUS
	}
	
	public Robot() {
		System.out.println("*** CONSTRUCTING ROBOT ***");
		// Initializing logging
		logger = new LogKitten("Robot", LogKitten.LEVEL_VERBOSE, LogKitten.LEVEL_FATAL);
		logger.v("Constructing", "Constructing");
		// Initialize serial interface
		serial = new SuperSerial();
		// Initialize sensors
		limitSwitches[Grabber.RIGHT_INNER_SWITCH] = new DigitalInput(RIGHT_INNER_SWITCH_PORT);
		limitSwitches[Grabber.LEFT_INNER_SWITCH] = new DigitalInput(LEFT_INNER_SWITCH_PORT);
		limitSwitches[Grabber.RIGHT_OUTER_SWITCH] = new DigitalInput(RIGHT_OUTER_SWITCH_PORT);
		limitSwitches[Grabber.LEFT_OUTER_SWITCH] = new DigitalInput(LEFT_OUTER_SWITCH_PORT);
		// Initialize Encoders
		frontLeftEncoder = new SuperEncoder(FRONT_LEFT_ENCODER_I2C_PORT);
		frontRightEncoder = new SuperEncoder(FRONT_RIGHT_ENCODER_I2C_PORT);
		backLeftEncoder = new SuperEncoder(BACK_LEFT_ENCODER_I2C_PORT);
		backRightEncoder = new SuperEncoder(BACK_RIGHT_ENCODER_I2C_PORT);
		winchEncoder = new SuperEncoder(WINCH_ENCODER_I2C_PORT);
		imu = new IMU(serial); // Initialize IMU
		udar = new UDAR(UDAR_I2C_PORT); // Initialize UDAR
		lidar = new LIDAR(); // Initialize LIDAR
		pdp = new PDP(); // Power Distribution Panel interface and logging.
		/* Lights! */
		// lightSequence = new DemoLightSequence(serial);
		/* Camera! */
		camera = new Camera();
		/* Action! */
		// Initialize movement controllers
		winch = new Winch(WINCH_PORT, winchEncoder); // Initialize Winch control
		grabber = new Grabber(GRABBER_PORT, limitSwitches, pdp); // Initialize Grabber control
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
		autonomousManager = new AutonomousManager(mecanumDrive, winch, grabber, align, camera, lidar, imu);
		modeManager = new ModeManager();
		// Drivers, operators, autonomous
		autonomous = autonomousManager.getAutonomous();
		// This list should include everything with a motor
		toDisable = new Disablable[] {winch, grabber, driver, operator, autonomous, frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel};
		alwaysUpdate = new Updatable[] {serial, imu, pdp, camera};
		alwaysUpdateSlow = new Updatable[] {};
	}
	
	public void robotInit() {
		System.out.println("*** INITIALIZING ***");
		logger.v("Initializing", "Initializing");
	}
	
	public void disabled() {
		System.out.println("*** DISABLED ***");
		logger.v("Disabled", "Disabled");
		RobotState state = RobotState.DISABLED;
		// IMU, PDP, and Camera should always update
		new Updater(state, alwaysUpdate, fastUpdatePeriod).start();
		// always slow updates
		new Updater(state, alwaysUpdateSlow, slowUpdatePeriod).start();
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
		new Updater(state, new Updatable[] {align}, slowUpdatePeriod).start(); // Controller and align are potentially slower
		// IMU, PDP, and Camera should always update
		new Updater(state, alwaysUpdate, fastUpdatePeriod).start();
		// always slow updates
		new Updater(state, alwaysUpdateSlow, slowUpdatePeriod).start();
		// These should have fast updates
		new Updater(state, new Updatable[] {autonomous, driver, operator, mecanumDrive, lidar, frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, grabber, winch}, fastUpdatePeriod).start();
		while (getRobotState() == state) {
			Timer.delay(0.01);
		}
	}
	
	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***");
		logger.v("Teleoperated", "Teleoperated");
		RobotState state = RobotState.OPERATOR;
		switch (modeManager.useMode()) {
			case ModeManager.TRAIN_PID_MODE:
				trainPID(state);
				return;
			case ModeManager.DUMP_LIDAR_MODE:
				dumpLIDAR(state);
				return;
			case ModeManager.NO_MODE:
				break;
			default:
				break;
		}
		operator = operatorManager.getOperator();
		driver = driverManager.getDriver();
		new Updater(state, new Updatable[] {align}, slowUpdatePeriod).start(); // align is potentially slower
		// IMU, PDP, and Camera should always update
		new Updater(state, alwaysUpdate, fastUpdatePeriod).start();
		// These should have fast updates
		new Updater(state, new Updatable[] {driver, operator, mecanumDrive, lidar, frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, grabber, winch}, fastUpdatePeriod).start();
		// always slow updates
		new Updater(state, alwaysUpdateSlow, slowUpdatePeriod).start();
		while (getRobotState() == state) {
			Timer.delay(0.01);
		}
	}
	
	public void trainPID(RobotState state) {
		System.out.println("*** TRAIN PID ***");
		logger.v("trainPID", "trainPID");
		// IMU, PDP, and Camera should always update
		new Updater(state, alwaysUpdate, fastUpdatePeriod).start();
		// always slow updates
		new Updater(state, alwaysUpdateSlow, slowUpdatePeriod).start();
		new Updater(state, new Updatable[] {frontLeftWheel, mecanumDrive}, fastUpdatePeriod).start();
		while (getRobotState() == state) {
			frontLeftWheel.setValue(0.1);
			logger.v("trainPID", "training cycle");
		}
	}
	
	public void dumpLIDAR(RobotState state) {
		System.out.println("*** DUMP LIDAR ***");
		logger.v("dumpLIDAR", "dumpLIDAR");
		// IMU, PDP, and Camera should always update
		new Updater(state, alwaysUpdate, fastUpdatePeriod).start();
		new Updater(state, alwaysUpdateSlow, slowUpdatePeriod).start();
		new Updater(state, new Updatable[] {lidar}, fastUpdatePeriod).start();
		while (getRobotState() == state) {
			for (int i = 0; i < 360; i++) {
				logger.w("LIDAR", "(" + Integer.toString(lidar.getXY(i)[0]) + ", " + Integer.toString(lidar.getXY(i)[1]) + ")");
			}
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
			logger.d("Run", "Starting");
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
			logger.d("Run", "Terminating");
		}
	}
}