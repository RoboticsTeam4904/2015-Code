package org.usfirst.frc.team4904.robot.input;


import org.usfirst.frc.team4904.robot.SuperI2C;
import edu.wpi.first.wpilibj.I2C;

public class MPUAccelGryo extends SuperI2C {
	public MPUAccelGryo(int port) {
		i2c = new I2C(I2C.Port.kOnboard, port);
	}
	
	public void init() {
		// Gyro setup
		i2c.write(0x24, 0x40); // Wait for Data at Slave0
		i2c.write(0x25, 0x8C); // Set i2c address at slave0 at 0x0C
		i2c.write(0x26, 0x02); // Set where reading at slave 0 starts
		i2c.write(0x27, 0x88); // set offset at start reading and enable
		i2c.write(0x28, 0x0C); // set i2c address at slv1 at 0x0C
		i2c.write(0x29, 0x0A); // Set where reading at slave 1 starts
		i2c.write(0x2A, 0x81); // Enable at set length to 1
		i2c.write(0x64, 0x01); // overvride register
		i2c.write(0x67, 0x03); // set delay rate
		i2c.write(0x01, 0x80);
		i2c.write(0x34, 0x04); // set i2c slv4 delay
		i2c.write(0x64, 0x00); // override register
		i2c.write(0x6A, 0x00); // clear usr setting
		i2c.write(0x64, 0x01); // override register
		i2c.write(0x6A, 0x20); // enable master i2c mode
		i2c.write(0x34, 0x13); // disable slv4
	}
	
	public void write(int registerAddress, int data) {
		i2c.write(registerAddress, data);
	}
	
	public byte test() {
		byte[] data = new byte[1];
		data[0] = (byte) 0x68;
		byte[] response = new byte[1];
		response[0] = -1;
		try {
			i2c.read(0x75, 1, response);
			return response[0];
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int read(int addrL, int addrH) {
		byte[] H = new byte[1];
		byte[] L = new byte[1];
		if (!i2c.read(addrH, 1, H)) {
			return -1;
		}
		if (!i2c.read(addrL, 1, L)) {
			return -1;
		}
		return (H[0] << 8) + L[0];
	}
	
	public void update() {
		// TODO Auto-generated method stub
	}
}
