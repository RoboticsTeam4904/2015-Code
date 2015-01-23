package org.usfirst.frc.team4904.robot;

import java.math.BigInteger;
import java.util.HashMap;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot implements IUpdatable{
	private static final int WINCH_PORT=4;
	private static final int GRABBER_PORT=5;
	private static final int JOYSTICK_PORT=0;
	private static final int CONTROLLER_PORT=1;
	private static enum State{
		STATE_TELEOPERATED, STATE_AUTONOMOUS, STATE_DISABLED
	}
	private LogitechJoystick stick;			// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private XboxController xboxController; 	// the Xbox 360 controller - driver
	
	private final UDAR udar;				// the UDAR (ultrasonic detection and ranging)
	private final IMU imu;
	private final LIDAR lidar;
	//Advanced movement controllers
	private final Winch winch;			// the Winch class takes care of moving to specific heights
	private final Grabber grabber;		// the grabber class takes care of openning and closing the grabber
	private final Mecanum mecanumDrive;	// the Mecanum class that takes care of the math required to use mecanum drive
	private final Driver driver;
	private final Operator operator;

	private final AutoAlign align;		// the AutoAlign class contains code to align the robot with totes and cans
	
	// Update system
	private final HashMap<State,IUpdatable[]> updatables=new HashMap<>();
	public static IUpdatable overallUpdate;
	private final SpeedController[] speedControllers;
	private final double updatePeriod = 0.005; // update every 0.005 seconds/5 milliseconds (200Hz)
	public Robot() {
		System.out.println("*** INITIALIZING ROBOT ***"); // Print the line "*** INITIALIZING ROBOT ***"
		
		//Initialize movement controllers
		winch = new Winch(WINCH_PORT); // Initialize Winch control
		grabber = new Grabber(GRABBER_PORT); // Initialize Grabber control
		mecanumDrive = new Mecanum(); // Initialize Mecanum control
		
		// Initialize joysticks (numbers correspond to value set by driver station)
		stick = new LogitechJoystick(JOYSTICK_PORT);
		xboxController = new XboxController(CONTROLLER_PORT);
		
		//Initialize sensors
		imu = new IMU(); 		// Initialize IMU
		udar = new UDAR(); // Initialize UDAR
		lidar = new LIDAR(); // Initalize LIDAR
		
		align = new AutoAlign(mecanumDrive, udar, lidar, imu, grabber); // Initialize AutoAlign system
		operator = new OperatorGriffin(stick,winch,align);
		driver = new DriverNathan(mecanumDrive,xboxController);

		updatables.put(State.STATE_TELEOPERATED,new IUpdatable[]{driver,operator,align,mecanumDrive,imu});
		updatables.put(State.STATE_DISABLED,new IUpdatable[]{imu});
		updatables.put(State.STATE_AUTONOMOUS,new IUpdatable[]{align,mecanumDrive,imu});
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
	private void disableMotors(){
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
		updateAll(updatables.get(getState()));
	}
	private State getState(){
		return isEnabled()?(isOperatorControl()?State.STATE_TELEOPERATED:(isAutonomous()?State.STATE_AUTONOMOUS:State.STATE_DISABLED)):State.STATE_DISABLED;
	}
	private void updateAll(IUpdatable[] toUpdate){
		for(IUpdatable updatable : toUpdate){
			updatable.update();
		}
	}
}
