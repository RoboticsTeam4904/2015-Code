package org.usfirst.frc.team4904.robot;


import org.usfirst.frc.team4904.robot.driver.DriverAutonomous;
import org.usfirst.frc.team4904.robot.driver.DriverNathan;
import org.usfirst.frc.team4904.robot.input.IMU;
import org.usfirst.frc.team4904.robot.input.LIDAR;
import org.usfirst.frc.team4904.robot.input.LogitechJoystick;
import org.usfirst.frc.team4904.robot.input.UDAR;
import org.usfirst.frc.team4904.robot.input.XboxController;
import org.usfirst.frc.team4904.robot.operator.OperatorAutonomous;
import org.usfirst.frc.team4904.robot.operator.OperatorGriffin;
import org.usfirst.frc.team4904.robot.output.Grabber;
import org.usfirst.frc.team4904.robot.output.Mecanum;
import org.usfirst.frc.team4904.robot.output.Winch;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

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
	
	private final LogitechJoystick stick;			// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private final XboxController xboxController; 	// the Xbox 360 controller - driver
	
	private final UDAR udar;				// the UDAR (ultrasonic detection and ranging)
	private final IMU imu;
	private final LIDAR lidar;
	
	// movement controllers
	private final Winch winch;			// the Winch class takes care of moving to specific heights
	private final Grabber grabber;		// the grabber class takes care of openning and closing the grabber
	private final Mecanum mecanumDrive;	// the Mecanum class that takes care of the math required to use mecanum drive
	private final SpeedController frontLeftWheel;
	private final SpeedController frontRightWheel;
	private final SpeedController backLeftWheel;
	private final SpeedController backRightWheel;
	private Driver driver;
	private Operator operator;
	private final Driver humanDriver;
	private final Operator humanOperator;
	private final Driver autonomousDriver;
	private final Operator autonomousOperator;
	private final AutonomousController controller;

	private final AutoAlign align;		// the AutoAlign class contains code to align the robot with totes and cans
	
	// Update system
	private final double fastUpdatePeriod = 0.005; // update every 0.005 seconds/5 milliseconds (200Hz)
	private final double slowUpdatePeriod = 0.5; // update every 0.005 seconds/5 milliseconds (200Hz)
	
	public Robot() {
		System.out.println("*** INITIALIZING ROBOT ***");
		
		//Initialize movement controllers
		winch = new Winch(WINCH_PORT);			// Initialize Winch control
		grabber = new Grabber(GRABBER_PORT);	// Initialize Grabber control -- only autoalign has access to this, by design
		// Initialize motor controllers with default ports
		frontLeftWheel = new VictorSP(FRONT_LEFT_WHEEL_PORT);
		frontRightWheel = new VictorSP(FRONT_RIGHT_WHEEL_PORT);
		backLeftWheel = new VictorSP(BACK_LEFT_WHEEL_PORT);
		backRightWheel = new VictorSP(BACK_RIGHT_WHEEL_PORT);
		mecanumDrive = new Mecanum(frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel);	// Initialize Mecanum control
		
		// Initialize joysticks (numbers correspond to value set by driver station)
		stick = new LogitechJoystick(JOYSTICK_PORT);
		xboxController = new XboxController(CONTROLLER_PORT);
		
		//Initialize sensors
		imu = new IMU();		// Initialize IMU
		udar = new UDAR();		// Initialize UDAR
		lidar = new LIDAR();	// Initalize LIDAR
		
		align = new AutoAlign(mecanumDrive, udar, lidar, imu, grabber); // Initialize AutoAlign system
		humanOperator = new OperatorGriffin(stick,winch,align);
		humanDriver = new DriverNathan(mecanumDrive,xboxController,align);
		controller=new AutonomousController(udar,imu,lidar);
		autonomousOperator = new OperatorAutonomous(winch,align,controller);
		autonomousDriver = new DriverAutonomous(mecanumDrive,controller,align);
	}
	
	public void disabled(){
		System.out.println("*** DISABLED ***");
		
		disableMotors(); // Disable all motors
	}
	
	private void disableMotors(){
		winch.set(0);
		grabber.set(0);
		frontLeftWheel.set(0);
		frontRightWheel.set(0);
		backLeftWheel.set(0);
		backRightWheel.set(0);
	}
	
	public void autonomous(){
		System.out.println("*** AUTONOMOUS ***");
		operator = autonomousOperator;
		driver = autonomousDriver;
		new Updater(2,new Updatable[]{controller,align},slowUpdatePeriod).start();
		new Updater(2,new Updatable[]{imu,driver,operator,mecanumDrive},fastUpdatePeriod).start();
		while(getRobotState()==2){
			Timer.delay(0.01);
		}
	}

	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***");
		operator = humanOperator;
		driver = humanDriver;
		new Updater(1,new Updatable[]{controller,align},slowUpdatePeriod).start();
		new Updater(1,new Updatable[]{imu,driver,operator,mecanumDrive},fastUpdatePeriod).start();
		while(getRobotState()==1){
			Timer.delay(0.01);
		}
	}
	private int getRobotState(){
		if(isEnabled() && isOperatorControl()){
			return 1;
		}
		if(isEnabled() && isAutonomous()) {
			return 2;
		}
		return 0;
	}
	private void updateSlow(){
		controller.update();
		align.update();
	}
	private void updateFast(){
		imu.update();
		driver.update();
		operator.update();
		mecanumDrive.update();
	}
	private static double time(){
		return ((double)System.currentTimeMillis())/1000;
	}
	private class Updater extends Thread{
		final int state;
		final Updatable[] toUpdate;
		final double updateSpeed;
		public Updater(int state, Updatable[] toUpdate, double updateSpeed){
			this.state=state;
			this.toUpdate=toUpdate;
			this.updateSpeed=updateSpeed;
		}
		public void run(){
			double desiredTime=time()+updateSpeed;
			while (getRobotState()==state) {
				for(Updatable update : toUpdate){
					update.update();
				}
				Timer.delay(desiredTime-time());//Wait until the time that this tick should end
				desiredTime+=updateSpeed;//Next tick should end updatePeriod seconds in the future
			}
		}
	}
}