package org.usfirst.frc.team4904.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends SampleRobot {
	private VictorSP leftFront;		// the victor controlling the left front wheel
	private VictorSP rightFront;	// the victor controlling the right front wheel
	private VictorSP leftBack;		// the victor controlling the left back wheel
	private VictorSP rightBack;		// the victor controlling the right back wheel
	
	private LogitechJoystick stick;	// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private Xbox xboxController;// the Xbox 360 controller - driver
	
	private UDAR udar;				// the UDAR (ultrasonic detection and ranging)
	private IMU imu;
	
	//Advanced movement controllers
	private Winch winch;			// the Winch class takes care of moving to specific heights
	private Grabber grabber;		// the grabber class takes care of openning and closing the grabber
	private Mecanum mecanumDrive;	// the Mecanum class that takes care of the math required to use mecanum drive
	private Driver driver;
	private Operator operator;
	private AutoAlign align;		// the AutoAlign class contains code to align the robot with totes and cans

	private final double updatePeriod = 0.005; // update every 0.005 seconds/5 milliseconds (200Hz)

	public Robot() {
		System.out.println("*** INITIALIZING ROBOT ***"); // Print the line "*** INITIALIZING ROBOT ***"
		
		// Initialize motor controllers (numbers correspond to PWM port on roboRIO)
		leftFront = new VictorSP(0);
		rightFront = new VictorSP(1);
		leftBack = new VictorSP(2);
		rightBack = new VictorSP(3);
		
		//Initialize movement controllers
		winch = new Winch(4); // Initialize Winch control
		grabber = new Grabber(5); // Initialize Grabber control
		mecanumDrive = new Mecanum(leftFront, rightFront, leftBack, rightBack, imu); // Initialize Mecanum control
		
		// Initialize joysticks (numbers correspond to value set by driver station)
		stick = new LogitechJoystick(0);
		xboxController = new Xbox(1);
		
		//Initialize sensors
		udar = new UDAR(); // Initialize UDAR
		imu = new IMU(); // Initialize IMU
		
		align = new AutoAlign(mecanumDrive, udar, imu, grabber); // Initialize AutoAlign system
		operator = new OperatorGriffin(stick,winch,align);
		driver = new DriverNathan(mecanumDrive,xboxController);
	}
	
	public void disabled(){
		System.out.println("*** DISABLED ***"); // Print the line "*** DISABLED ***"
		
		// Disable all motors
		leftFront.set(0); // Disable left front motor
		rightFront.set(0); // Disable right front motor
		leftBack.set(0); // Disable left back motor
		rightBack.set(0); // Disable right back motor
		winch.set(0); // Disable winch
		grabber.set(0); // Disable grabber
		
		while(isDisabled()){ // While the robot is set to disabled
			imu.update(); // Update IMU
			Timer.delay(updatePeriod);
		}
	}
	
	public void autonomous(){
		System.out.println("*** AUTONOMOUS ***"); // Print the line "*** AUTONOMOUS ***"
		
		while(isAutonomous() && isEnabled()){ // While the robot is set to autonomous and is enabled
			leftFront.set(1);
			
			
			mecanumDrive.update();	// Update the mecanum drive
			imu.update();
			Timer.delay(updatePeriod);	// wait delay specified by updatePeriod to the next update

		}
	}

	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***"); // Print the line "*** TELEOPERATED ***"
		
		while (isOperatorControl() && isEnabled()) { // While the robot is set to operator control and is enabled
			leftFront.set(stick.getY());
			
			driver.update();
			operator.update();
			mecanumDrive.update(); // Update the mecanum drive
			imu.update();
			Timer.delay(updatePeriod);	// wait delay specified by updatePeriod to the next update
		}
		
	}
}
