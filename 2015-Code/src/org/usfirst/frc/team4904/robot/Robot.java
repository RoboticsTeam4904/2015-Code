package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends SampleRobot implements Updatable{
	
	private LogitechJoystick stick;			// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private XboxController xboxController; 	// the Xbox 360 controller - driver
	
	// Sensor classes
	private UDAR udar;				// the UDAR (ultrasonic detection and ranging)
	private IMU imu;				// the IMU (inertial measurement unit)
	private LIDAR lidar;			// the LIDAR (light detection and ranging)
	
	// movement controllers
	private Winch winch;			// the Winch class takes care of moving to specific heights
	private Grabber grabber;		// the grabber class takes care of openning and closing the grabber
	private Mecanum mecanumDrive;	// the Mecanum class that takes care of the math required to use mecanum drive
	private SpeedController[] speedControllers;
	
	// Control classes
	private Driver driver;			// the driver class 
	private Operator operator;		// the operator class
	private AutoAlign align;		// the AutoAlign class contains code to align the robot with totes and cans
	
	// Update system
	private Updatable[] updatables;
	public static Updatable overallUpdate;
	
	private final double updatePeriod = 0.005; // update every 0.005 seconds/5 milliseconds (200Hz)
	public Robot() {
		System.out.println("*** INITIALIZING ROBOT ***"); // Print the line "*** INITIALIZING ROBOT ***"
		
		//Initialize movement controllers
		winch = new Winch(4); // Initialize Winch control
		grabber = new Grabber(5); // Initialize Grabber control
		imu=new IMU();
		mecanumDrive = new Mecanum(imu); // Initialize Mecanum control
		
		// Initialize joysticks (numbers correspond to value set by driver station)
		stick = new LogitechJoystick(0);
		xboxController = new XboxController(1);
		
		// Initialize sensors
		udar = new UDAR(); 		// Initialize UDAR
		imu = new IMU(); 		// Initialize IMU
		
		// Initialize control system
		align = new AutoAlign(mecanumDrive, udar, imu, grabber);	// Initialize AutoAlign system
		operator = new OperatorGriffin(stick,winch,align); 			// Initialize operator (who needs stick, winch, and align)
		driver = new DriverNathan(mecanumDrive,xboxController); 	// Initialize driver (who needs xbox and mecanum)
		
		
		updatables=new Updatable[]{driver,operator,mecanumDrive,imu};
		speedControllers=new SpeedController[]{winch,grabber};
		overallUpdate=this;
	}
	
	public void disabled(){
		System.out.println("*** DISABLED ***"); // Print the line "*** DISABLED ***"
		
		// Disable all motors
		disableMotors();
		
		while(isDisabled()){ // While the robot is set to disabled
			doOverallUpdate();
			Timer.delay(updatePeriod);
		}
	}
	public void disableMotors(){
		for(SpeedController sc : speedControllers){
			sc.set(0);
		}
	}
	public void autonomous(){
		System.out.println("*** AUTONOMOUS ***"); // Print the line "*** AUTONOMOUS ***"
		
		while(isAutonomous() && isEnabled()){ // While the robot is set to autonomous and is enabled
			
			
			doOverallUpdate();
			Timer.delay(updatePeriod);	// wait delay specified by updatePeriod to the next update

		}
	}

	public synchronized void operatorControl() {
		System.out.println("*** TELEOPERATED ***"); // Print the line "*** TELEOPERATED ***"
		
		while (isOperatorControl() && isEnabled()) { // While the robot is set to operator control and is enabled
			
			doOverallUpdate();
			Timer.delay(updatePeriod);	// wait delay specified by updatePeriod to the next update
		}
		
	}
	public static void doOverallUpdate(){
		overallUpdate.update();
	}
	public void update(){
		if(isOperatorControl() && isEnabled()){
			updateAll();
		}else if (isAutonomous() && isEnabled()){
			updatables[2].update();
			updatables[3].update();
		}else{
			updatables[3].update();
		}
	}
	public void updateAll(){
		for(Updatable updatable : updatables){
			updatable.update();
		}
	}
}
