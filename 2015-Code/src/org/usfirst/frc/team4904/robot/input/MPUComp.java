package org.usfirst.frc.team4904.robot.input;


import org.usfirst.frc.team4904.robot.SuperI2C;
import edu.wpi.first.wpilibj.I2C;

public class MPUComp extends SuperI2C {
	public MPUComp(int port) {
		i2c = new I2C(I2C.Port.kOnboard, port);
	}
	
	public void init() {
		// Mag setup
		i2c.write(0x0A, 0x00); // PowerDownMode
		i2c.write(0x0A, 0x0F); // SelfTest
		i2c.write(0x0A, 0x00); // PowerDownMode
	}
	
	public void update() {
		// TODO Auto-generated method stub
	}
	
	public void disable() {}
}
