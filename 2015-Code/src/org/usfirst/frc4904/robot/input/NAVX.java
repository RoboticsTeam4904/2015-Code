package org.usfirst.frc4904.robot.input;


import com.kauailabs.navx_mxp.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

public class NAVX extends AHRS {
	public NAVX() {
		super(new SerialPort(57600, SerialPort.Port.kUSB), (byte) 50);
	}
	
	public float getYaw() {
		float yaw = super.getYaw();
		if (yaw < 0) return 360.00f + yaw;
		else return yaw;
	}
	
	public float getPitch() {
		float pitch = super.getPitch();
		if (pitch < 0) return 360.00f + pitch;
		else return pitch;
	}
	
	public float getRoll() {
		float roll = super.getRoll();
		if (roll < 0) return 360.00f + roll;
		else return roll;
	}
}
