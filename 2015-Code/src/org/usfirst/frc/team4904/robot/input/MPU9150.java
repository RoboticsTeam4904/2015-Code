package org.usfirst.frc.team4904.robot.input;


import org.usfirst.frc.team4904.robot.Port;

public class MPU9150 extends Port {
	public final int MPU9150_PWR_MGMT_1 = 0x6B; // R/W
	public final int MPU9150_PWR_MGMT_2 = 0x6C; // R/W
	// taken from http://playground.arduino.cc/Main/MPU-9150
	// modified to be Java syntax
	private int[] cmps = new int[3];
	private int[] accl = new int[3];
	private int[] gyro = new int[3];
	private int temp; // Temperature, not temporary. Don't kill Erik.
	// I2C objects
	private MPUAccelGryo accelGryo;
	private MPUComp compass;
	
	public MPU9150() {
		// Initialize I2C
		accelGryo = new MPUAccelGryo(0x68);
		compass = new MPUComp(0x0C);
		accelGryo.write(MPU9150_PWR_MGMT_1, 0);
		// Data variable
		data = new double[10];
	}
	
	public void init() {
		// Extremely modified from http://playground.arduino.cc/Main/MPU-9150
		accelGryo.init();
		compass.init();
	}
	
	public byte test() {
		return accelGryo.test();
	}
	
	public void readAcc() {}
	
	// //////////////////////////
	// Low level I2C functions //
	// //////////////////////////
	public void update() {}
}
