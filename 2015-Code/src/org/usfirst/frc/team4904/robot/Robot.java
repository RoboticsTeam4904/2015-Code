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
	private Talon winch;			// the talon controlling the winch
	private Talon grabber;			// the talon controlling the fork's grabber
	
	private LogitechJoystick blackStick;	// the ATK3 Logitech joystick (left hand) - operator
	private LogitechJoystick whiteStick;	// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private Xbox xboxController;// the Xbox 360 controller - driver
	
	private UDAR udar;				// the UDAR (ultrasonic detection and ranging)
	private IMU imu;
	private Mecanum mecanumDrive;	// the Mecanum class that takes care of the math required to use mecanum drive
	
	private AutoAlign align;

	private final double updatePeriod = 0.005; // update every 0.005 seconds/5 milliseconds (200Hz)

	public Robot() {
		System.out.println("*** INITIALIZING ROBOT ***");
		
		// Initialize motor controllers (numbers correspond to PWM port on roboRIO)
		leftFront = new VictorSP(0);
		rightFront = new VictorSP(1);
		leftBack = new VictorSP(2);
		rightBack = new VictorSP(3);
		winch = new Talon(4);
		grabber = new Talon(5);
		
		// Initialize joysticks (numbers correspond to value set by driver station)
		blackStick = new LogitechJoystick(0);
		//whiteStick = new LogitechJoystick(1);
		//xboxController = new Xbox(2);
		
		udar = new UDAR(); // Initialize UDAR
		imu = new IMU(); // Initialize IMU
		mecanumDrive = new Mecanum(leftFront, rightFront, leftBack, rightBack, imu); // Initialize Mecanum control
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
		}
	}
	
	public void autonomous(){
		System.out.println("*** AUTONOMOUS ***"); // Print the line "*** AUTONOMOUS ***"
		
		while(isAutonomous() && isEnabled()){ // While the robot is set to autonomous and is enabled
			leftFront.set(1);
			
			
			mecanumDrive.update();	// Update the mecanum drive
			Timer.delay(updatePeriod);	// wait delay specified by updatePeriod to the next update

		}
	}

	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***"); // Print the line "*** TELEOPERATED ***"
		
		while (isOperatorControl() && isEnabled()) { // While the robot is set to operator control and is enabled
			leftFront.set(blackStick.getY());
			
			mecanumDrive.update(); // Update the mecanum drive
			Timer.delay(updatePeriod);	// wait delay specified by updatePeriod to the next update
		}
		
	}
}
