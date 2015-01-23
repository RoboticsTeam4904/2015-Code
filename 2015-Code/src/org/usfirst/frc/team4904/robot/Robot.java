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
	private final LogitechJoystick stick;			// the X3D Extreme3DPro Logitech joystick (right hand) - operator
	private final XboxController xboxController; 	// the Xbox 360 controller - driver
	
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

		updatables.put(State.STATE_TELEOPERATED,new IUpdatable[]{driver,operator,align,mecanumDrive,imu});// In teleoperated mode, update everything
		updatables.put(State.STATE_DISABLED,new IUpdatable[]{imu});// In disabled mode, only update the IMU. The IMU always needs to be updated
		updatables.put(State.STATE_AUTONOMOUS,new IUpdatable[]{align,mecanumDrive,imu});// In autonomous mode, update autoalign, mecanumdrive, and IMU
		// TODO Create DriverAutonomous and OperatorAutonomous classes
		
		speedControllers=new SpeedController[]{winch,grabber};
	}
	
	public void disabled(){
		System.out.println("*** DISABLED ***"); // Print the line "*** DISABLED ***"
		
		// Disable all motors
		disableMotors();
		
		doLoop(State.STATE_DISABLED);
	}
	private void disableMotors(){// TODO This function also should disable the mecanum drives
		for(SpeedController sc : speedControllers){
			sc.set(0);
		}
	}
	public void autonomous(){
		System.out.println("*** AUTONOMOUS ***"); // Print the line "*** AUTONOMOUS ***"
		doLoop(State.STATE_AUTONOMOUS);
	}

	public void operatorControl() {
		System.out.println("*** TELEOPERATED ***"); // Print the line "*** TELEOPERATED ***"
		doLoop(State.STATE_TELEOPERATED);
	}
	private void doLoop(State desiredState){
		while(getState()==desiredState){//Basically, run until the robot changes state
			update();//This updates everything that needs to be updated, based on the current state
			Timer.delay(updatePeriod);
		}
	}
	public void update(){// TODO have doLoop pass the state to this function so getState() is only called once per tick.
		updateAll(updatables.get(getState()));// Look up in the HashMap what needs to be updated in the current state
	}
	private State getState(){
		return isEnabled()?(isOperatorControl()?State.STATE_TELEOPERATED:(isAutonomous()?State.STATE_AUTONOMOUS:State.STATE_DISABLED)):State.STATE_DISABLED;
	}
	private static void updateAll(IUpdatable[] toUpdate){//Update everything in the list
		for(IUpdatable updatable : toUpdate){
			updatable.update();
		}
	}
}
