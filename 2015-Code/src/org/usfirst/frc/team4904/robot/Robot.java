package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends SampleRobot implements Updatable{
	
	private final LogitechJoystick stick;	// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private final XboxController xboxController; 	// the Xbox 360 controller - driver
	
	private final UDAR udar;				// the UDAR (ultrasonic detection and ranging)
	private final IMU imu;
	
	//Advanced movement controllers
	private final Winch winch;			// the Winch class takes care of moving to specific heights
	private final Grabber grabber;		// the grabber class takes care of openning and closing the grabber
	private final Mecanum mecanumDrive;	// the Mecanum class that takes care of the math required to use mecanum drive
	private Driver driver;
	private Operator operator;
	private AutoAlign align;		// the AutoAlign class contains code to align the robot with totes and cans
	private Updatable[] updatables;
	private final double updatePeriod = 0.005; // update every 0.005 seconds/5 milliseconds (200Hz)
	private SpeedController[] speedControllers;
	public static Updatable overallUpdate;
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
		
		//Initialize sensors
		udar = new UDAR(); // Initialize UDAR
		
		
		align = new AutoAlign(mecanumDrive, udar, imu, grabber); // Initialize AutoAlign system
		operator = new OperatorGriffin(stick,winch,align);
		driver = new DriverNathan(mecanumDrive,xboxController);
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
