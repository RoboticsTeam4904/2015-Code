package org.usfirst.frc.team4904.robot.input;

import edu.wpi.first.wpilibj.I2C;

public class UDAR {
	
	I2C i2c;
	
	public UDAR(){

		i2c = new I2C(I2C.Port.kOnboard, 168); // Initialize I2C
	}
	
}