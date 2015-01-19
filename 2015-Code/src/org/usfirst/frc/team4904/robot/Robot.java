package org.usfirst.frc.team4904.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.I2C;

public class Robot extends SampleRobot {
	private VictorSP leftFront;		// the victor controlling the left front wheel
	private VictorSP rightFront;	// the victor controlling the right front wheel
	private VictorSP leftBack;		// the victor controlling the left back wheel
	private VictorSP rightBack;		// the victor controlling the right back wheel
	private Talon winch;			// the talon controlling the winch
	private Talon grabber;			// the talon controlling the fork's grabber
	
	private Joystick blackStick;	// the ATK3 Logitech joystick (left hand) - operator
	private Joystick whiteStick;	// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private Joystick xboxController;// the Xbox 360 controller - driver
	
	private I2C udar;				// I2C connector for the UDAR (ultrasonic detection and ranging)
	private IMU imu;
	private Mecanum mecanumDrive;	// the Mecanum class that takes care of the math required to use mecanum drive

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
		blackStick = new Joystick(0);
		//whiteStick = new Joystick(1);
		//xboxController = new Joystick(2);
		
		udar = new I2C(I2C.Port.kOnboard, 168); // Initialize I2C
		imu = new IMU(); // Initialize IMU
		mecanumDrive = new Mecanum(leftFront, rightFront, leftBack, rightBack, imu); // Initialize Mecanum control
	}
	
	public void disabled(){
		System.out.println("*** DISABLED ***");
		
		// Disable all motors
		leftFront.set(0);
		rightFront.set(0);
		leftBack.set(0);
		rightBack.set(0);
		winch.set(0);
		grabber.set(0);
	}
	
	public void autonomous(){
		System.out.println("*** AUTONOMOUS ***");
		
		leftFront.set(1);
	}

	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***");
		
		while (isOperatorControl() && isEnabled()) {
			leftFront.set(blackStick.getY());
			
			Timer.delay(updatePeriod);	// wait delay specified by updatePeriod to the next update
		}
		
	}
}
