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
import org.usfirst.frc4904.robot.input.UDAR;
import org.usfirst.frc4904.robot.input.XboxController;
import org.usfirst.frc4904.robot.operator.Operator;
import org.usfirst.frc4904.robot.operator.OperatorManager;
import org.usfirst.frc4904.robot.output.DampenedMotor;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Mecanum;
import org.usfirst.frc4904.robot.output.Winch;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
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
	private static final int RIGHT_OUTER_SWITCH_PORT = 0;
	private static final int LEFT_OUTER_SWITCH_PORT = 1;
	private static final int RIGHT_INNER_SWITCH_PORT = 8;
	private static final int LEFT_INNER_SWITCH_PORT = 9;
	// Various I2C ports
	private static final int WINCH_ENCODER_PORT_1 = 3;
	private static final int WINCH_ENCODER_PORT_2 = 2;
	private static final int UDAR_I2C_PORT = 4;
	private static final double WINCH_P_COEFFICIENT = -0.7;
	private static final double WINCH_I_COEFFICIENT = 0.00;
	private static final double WINCH_D_COEFFICIENT = 0.00;
	private static final double MECANUM_P_COEFFICIENT = 0.1;
	private static final double MECANUM_I_COEFFICIENT = 0.1;
	private static final double MECANUM_D_COEFFICIENT = 0.1;
	private final LogitechJoystick stick; // the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private final XboxController xboxController; // the Xbox 360 controller - driver
	// Input devices
	private final UDAR udar; // the UDAR (ultrasonic detection and ranging)
	private final IMU imu;
	private final LIDAR lidar;
	private final DigitalInput limitSwitches[] = new DigitalInput[4];
	private final Camera camera;
	// private final DemoLightSequence lightSequence;
	private final Encoder winchEncoder;
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
	private volatile Driver driver;
	private volatile Operator operator;
	private volatile Autonomous autonomous;
	private final AutoAlign align; // the AutoAlign class contains code to align the robot with totes and cans
	// Update system
	public static final double fastUpdatePeriod = 0.02; // update every 0.01 seconds/10 milliseconds (100Hz) This is IMU speed
	public static final double slowUpdatePeriod = 0.2; // update every 0.2 seconds/200 milliseconds (5Hz)
	// Logging system
	private final LogKitten logger;
	// Disables
	private final Enablable[] toEnable;
	private final Disablable[] toDisable;
	private final Updatable[] alwaysUpdate;
	private final Updatable[] alwaysUpdateSlow;
	// Power distribution board
	private final PDP pdp;
	
	public enum RobotState {
		DISABLED, OPERATOR, AUTONOMOUS
	}
	
	public Robot() {
		System.out.println("*** CONSTRUCTING ROBOT ***");
		// Initializing logging
		logger = new LogKitten(LogKitten.LEVEL_VERBOSE);
		logger.v("Constructing");
		// Initialize serial interface
		// Initialize sensors
		limitSwitches[Grabber.RIGHT_OUTER_SWITCH] = new DigitalInput(RIGHT_OUTER_SWITCH_PORT);
		limitSwitches[Grabber.LEFT_OUTER_SWITCH] = new DigitalInput(LEFT_OUTER_SWITCH_PORT);
		limitSwitches[Grabber.RIGHT_INNER_SWITCH] = new DigitalInput(RIGHT_INNER_SWITCH_PORT);
		limitSwitches[Grabber.LEFT_INNER_SWITCH] = new DigitalInput(LEFT_INNER_SWITCH_PORT);
		// Initialize Encoders
		winchEncoder = new Encoder(WINCH_ENCODER_PORT_1, WINCH_ENCODER_PORT_2);
		imu = new IMU(); // Initialize IMU
		udar = new UDAR(UDAR_I2C_PORT); // Initialize UDAR
		lidar = new LIDAR(); // Initialize LIDAR
		pdp = new PDP(); // Power Distribution Panel interface and logging.
		camera = new Camera();
		/* Action! */
		// Initialize movement controllers
		winch = new Winch(WINCH_PORT, winchEncoder, WINCH_P_COEFFICIENT, WINCH_I_COEFFICIENT, WINCH_D_COEFFICIENT); // Initialize Winch control
		grabber = new Grabber(GRABBER_PORT, limitSwitches, pdp); // Initialize Grabber control
		// Initialize motor controllers with default ports
		frontLeftWheel = new DampenedMotor(FRONT_LEFT_WHEEL_PORT);
		frontRightWheel = new DampenedMotor(FRONT_RIGHT_WHEEL_PORT);
		backLeftWheel = new DampenedMotor(BACK_LEFT_WHEEL_PORT);
		backRightWheel = new DampenedMotor(BACK_RIGHT_WHEEL_PORT);
		mecanumDrive = new Mecanum(frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, imu, MECANUM_P_COEFFICIENT, MECANUM_I_COEFFICIENT, MECANUM_D_COEFFICIENT); // Initialize Mecanum control
		// Initialize joysticks (numbers correspond to value set by driver station)
		stick = new LogitechJoystick(JOYSTICK_PORT);
		xboxController = new XboxController(CONTROLLER_PORT);
		// Initalize subsystems
		align = new AutoAlign(mecanumDrive, udar, lidar, imu); // Initialize AutoAlign system
		// Initialize managers
		driverManager = new DriverManager(mecanumDrive, align, xboxController);
		operatorManager = new OperatorManager(stick, winch, align, grabber);
		autonomousManager = new AutonomousManager(winch, grabber, align, camera, lidar, imu);
		// Drivers, operators, autonomous
		autonomous = autonomousManager.getSelected();
		// This list should include everything with a motor
		toDisable = new Disablable[] {winch, grabber, driver, operator, autonomous, frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel};
		toEnable = new Enablable[] {mecanumDrive, winch};
		alwaysUpdate = new Updatable[] {imu, pdp};
		alwaysUpdateSlow = new Updatable[] {};
	}
	
	public void robotInit() {
		System.out.println("*** INITIALIZING ***");
		logger.v("Initializing");
		imu.zero();
	}
	
	public void disabled() {
		System.out.println("*** DISABLED ***");
		logger.v("Disabled");
		RobotState state = RobotState.DISABLED;
		startAlwaysUpdates(state);
		while (isDisabled()) {
			for (Disablable implementsdisable : toDisable) {
				if (implementsdisable != null) {
					implementsdisable.disable();
				}
			}
			Timer.delay(0.01);
		}
	}
	
	public void autonomous() {
		System.out.println("*** AUTONOMOUS ***");
		logger.v("Autonomous");
		RobotState state = RobotState.AUTONOMOUS;
		autonomous = autonomousManager.getSelected();
		driver = autonomous.getAutoDriver();
		operator = autonomous.getAutoOperator();
		for (Enablable implementsenable : toEnable) {
			if (implementsenable != null) {
				implementsenable.enable();
			}
		}
		new Updater(this, state, new Updatable[] {camera}, slowUpdatePeriod).start(); // Controller and align are potentially slower
		startAlwaysUpdates(state);
		// These should have fast updates
		new Updater(this, state, new Updatable[] {autonomous, driver, operator, mecanumDrive, lidar, frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, grabber}, fastUpdatePeriod).start();
		while (isAutonomous() && isEnabled()) {
			Timer.delay(0.01);
		}
	}
	
	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***");
		logger.v("Teleoperated");
		RobotState state = RobotState.OPERATOR;
		for (Enablable implementsenable : toEnable) {
			if (implementsenable != null) {
				implementsenable.enable();
			}
		}
		operator = operatorManager.getSelected();
		driver = driverManager.getSelected();
		new Updater(this, state, new Updatable[] {}, slowUpdatePeriod).start(); // align is potentially slower
		startAlwaysUpdates(state);
		// These should have fast updates
		new Updater(this, state, new Updatable[] {driver, operator, mecanumDrive, lidar, frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, grabber}, fastUpdatePeriod).start();
		while (isOperatorControl() && isEnabled()) {
			Timer.delay(0.01);
		}
	}
	
	public void startAlwaysUpdates(RobotState state) {
		// IMU, and PDP should always update
		new Updater(this, state, alwaysUpdate, fastUpdatePeriod).start();
		// always slow updates
		new Updater(this, state, alwaysUpdateSlow, slowUpdatePeriod).start();
	}
	
	public void trainMecanumPID(RobotState state) {
		System.out.println("*** TRAIN PID ***");
		logger.v("trainPID");
		startAlwaysUpdates(state);
		new Updater(this, state, new Updatable[] {frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel, mecanumDrive}, fastUpdatePeriod).start();
		while (getRobotState() == state) {
			logger.v("training cycle");
			mecanumDrive.setDesiredTurnSpeed(0.2);
			mecanumDrive.setDesiredXYSpeed(0, 0);
			Timer.delay(3);
		}
	}
	
	public RobotState getRobotState() {
		if (isDisabled()) {
			return RobotState.DISABLED;
		}
		if (isOperatorControl() && isEnabled()) {
			return RobotState.OPERATOR;
		}
		if (isAutonomous() && isEnabled()) {
			return RobotState.AUTONOMOUS;
		}
		return RobotState.DISABLED;
	}
	
	public static double time() {
		return ((double) System.currentTimeMillis()) / 1000D;
	}
}