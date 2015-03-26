package org.usfirst.frc4904.robot.input;


import com.kauailabs.navx_mxp.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

public class NAVX extends AHRS {
	public NAVX() {
		super(new SerialPort(57600, SerialPort.Port.kMXP), (byte) 50);
	}
}
