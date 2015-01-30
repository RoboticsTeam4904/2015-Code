package org.usfirst.frc.team4904.robot.input;


import edu.wpi.first.wpilibj.I2C;

public class MPU9150 {
	// taken from http://playground.arduino.cc/Main/MPU-9150
	// modified to be Java syntax
	private final int MPU9150_SELF_TEST_X_2 = 0x0D; // R/W
	private final int MPU9150_SELF_TEST_Y = 0x0E; // R/W
	private final int MPU9150_SELF_TEST_X = 0x0F; // R/W
	private final int MPU9150_SELF_TEST_A = 0x10; // R/W
	private final int MPU9150_SMPLRT_DIV = 0x19; // R/W
	private final int MPU9150_CONFIG = 0x1A; // R/W
	private final int MPU9150_GYRO_CONFIG = 0x1B; // R/W
	private final int MPU9150_ACCEL_CONFIG = 0x1C; // R/W
	private final int MPU9150_FF_THR = 0x1D; // R/W
	private final int MPU9150_FF_DUR = 0x1E; // R/W
	private final int MPU9150_MOT_THR = 0x1F; // R/W
	private final int MPU9150_MOT_DUR = 0x20; // R/W
	private final int MPU9150_ZRMOT_THR = 0x21; // R/W
	private final int MPU9150_ZRMOT_DUR = 0x22; // R/W
	private final int MPU9150_FIFO_EN = 0x23; // R/W
	private final int MPU9150_I2C_MST_CTRL = 0x24; // R/W
	private final int MPU9150_I2C_SLV0_ADDR = 0x25; // R/W
	private final int MPU9150_I2C_SLV0_REG = 0x26; // R/W
	private final int MPU9150_I2C_SLV0_CTRL = 0x27; // R/W
	private final int MPU9150_I2C_SLV1_ADDR = 0x28; // R/W
	private final int MPU9150_I2C_SLV1_REG = 0x29; // R/W
	private final int MPU9150_I2C_SLV1_CTRL = 0x2A; // R/W
	private final int MPU9150_I2C_SLV2_ADDR = 0x2B; // R/W
	private final int MPU9150_I2C_SLV2_REG = 0x2C; // R/W
	private final int MPU9150_I2C_SLV2_CTRL = 0x2D; // R/W
	private final int MPU9150_I2C_SLV3_ADDR = 0x2E; // R/W
	private final int MPU9150_I2C_SLV3_REG = 0x2F; // R/W
	private final int MPU9150_I2C_SLV3_CTRL = 0x30; // R/W
	private final int MPU9150_I2C_SLV4_ADDR = 0x31; // R/W
	private final int MPU9150_I2C_SLV4_REG = 0x32; // R/W
	private final int MPU9150_I2C_SLV4_DO = 0x33; // R/W
	private final int MPU9150_I2C_SLV4_CTRL = 0x34; // R/W
	private final int MPU9150_I2C_SLV4_DI = 0x35; // R
	private final int MPU9150_I2C_MST_STATUS = 0x36; // R
	private final int MPU9150_INT_PIN_CFG = 0x37; // R/W
	private final int MPU9150_INT_ENABLE = 0x38; // R/W
	private final int MPU9150_INT_STATUS = 0x3A; // R
	private final int MPU9150_ACCEL_XOUT_H = 0x3B; // R
	private final int MPU9150_ACCEL_XOUT_L = 0x3C; // R
	private final int MPU9150_ACCEL_YOUT_H = 0x3D; // R
	private final int MPU9150_ACCEL_YOUT_L = 0x3E; // R
	private final int MPU9150_ACCEL_ZOUT_H = 0x3F; // R
	private final int MPU9150_ACCEL_ZOUT_L = 0x40; // R
	private final int MPU9150_TEMP_OUT_H = 0x41; // R
	private final int MPU9150_TEMP_OUT_L = 0x42; // R
	private final int MPU9150_GYRO_XOUT_H = 0x43; // R
	private final int MPU9150_GYRO_XOUT_L = 0x44; // R
	private final int MPU9150_GYRO_YOUT_H = 0x45; // R
	private final int MPU9150_GYRO_YOUT_L = 0x46; // R
	private final int MPU9150_GYRO_ZOUT_H = 0x47; // R
	private final int MPU9150_GYRO_ZOUT_L = 0x48; // R
	private final int MPU9150_EXT_SENS_DATA_00 = 0x49; // R
	private final int MPU9150_EXT_SENS_DATA_01 = 0x4A; // R
	private final int MPU9150_EXT_SENS_DATA_02 = 0x4B; // R
	private final int MPU9150_EXT_SENS_DATA_03 = 0x4C; // R
	private final int MPU9150_EXT_SENS_DATA_04 = 0x4D; // R
	private final int MPU9150_EXT_SENS_DATA_05 = 0x4E; // R
	private final int MPU9150_EXT_SENS_DATA_06 = 0x4F; // R
	private final int MPU9150_EXT_SENS_DATA_07 = 0x50; // R
	private final int MPU9150_EXT_SENS_DATA_08 = 0x51; // R
	private final int MPU9150_EXT_SENS_DATA_09 = 0x52; // R
	private final int MPU9150_EXT_SENS_DATA_10 = 0x53; // R
	private final int MPU9150_EXT_SENS_DATA_11 = 0x54; // R
	private final int MPU9150_EXT_SENS_DATA_12 = 0x55; // R
	private final int MPU9150_EXT_SENS_DATA_13 = 0x56; // R
	private final int MPU9150_EXT_SENS_DATA_14 = 0x57; // R
	private final int MPU9150_EXT_SENS_DATA_15 = 0x58; // R
	private final int MPU9150_EXT_SENS_DATA_16 = 0x59; // R
	private final int MPU9150_EXT_SENS_DATA_17 = 0x5A; // R
	private final int MPU9150_EXT_SENS_DATA_18 = 0x5B; // R
	private final int MPU9150_EXT_SENS_DATA_19 = 0x5C; // R
	private final int MPU9150_EXT_SENS_DATA_20 = 0x5D; // R
	private final int MPU9150_EXT_SENS_DATA_21 = 0x5E; // R
	private final int MPU9150_EXT_SENS_DATA_22 = 0x5F; // R
	private final int MPU9150_EXT_SENS_DATA_23 = 0x60; // R
	private final int MPU9150_MOT_DETECT_STATUS = 0x61; // R
	private final int MPU9150_I2C_SLV0_DO = 0x63; // R/W
	private final int MPU9150_I2C_SLV1_DO = 0x64; // R/W
	private final int MPU9150_I2C_SLV2_DO = 0x65; // R/W
	private final int MPU9150_I2C_SLV3_DO = 0x66; // R/W
	private final int MPU9150_I2C_MST_DELAY_CTRL = 0x67; // R/W
	private final int MPU9150_SIGNAL_PATH_RESET = 0x68; // R/W
	private final int MPU9150_MOT_DETECT_CTRL = 0x69; // R/W
	private final int MPU9150_USER_CTRL = 0x6A; // R/W
	private final int MPU9150_PWR_MGMT_1 = 0x6B; // R/W
	private final int MPU9150_PWR_MGMT_2 = 0x6C; // R/W
	private final int MPU9150_FIFO_COUNTH = 0x72; // R/W
	private final int MPU9150_FIFO_COUNTL = 0x73; // R/W
	private final int MPU9150_FIFO_R_W = 0x74; // R/W
	private final int MPU9150_WHO_AM_I = 0x75; // R
	// ////////////////////////////////////////////////
	// MPU9150 Compass
	private final int MPU9150_CMPS_XOUT_L = 0x4A; // R
	private final int MPU9150_CMPS_XOUT_H = 0x4B; // R
	private final int MPU9150_CMPS_YOUT_L = 0x4C; // R
	private final int MPU9150_CMPS_YOUT_H = 0x4D; // R
	private final int MPU9150_CMPS_ZOUT_L = 0x4E; // R
	private final int MPU9150_CMPS_ZOUT_H = 0x4F; // R
	// ////////////////////////////////////////////////
	private int[] cmps = new int[3];
	private int[] accl = new int[3];
	private int[] gyro = new int[3];
	private int temp;
	// I2C objects
	private I2C accelMag;
	private I2C compass;
	
	public MPU9150() {
		// Initialize I2C
		accelMag = new I2C(I2C.Port.kOnboard, 0x0C);
		compass = new I2C(I2C.Port.kOnboard, 0x68);
		accelMag.write(MPU9150_PWR_MGMT_1, 0);
	}
	
	public double[] read() {
		double[] data = new double[10];
		data[0] = (this.readSensor(MPU9150_TEMP_OUT_L, MPU9150_TEMP_OUT_H) + 12412.0) / 340.0;
		data[1] = this.readSensor(MPU9150_CMPS_XOUT_L, MPU9150_CMPS_XOUT_H);
		data[2] = this.readSensor(MPU9150_CMPS_YOUT_L, MPU9150_CMPS_YOUT_H);
		data[3] = this.readSensor(MPU9150_CMPS_ZOUT_L, MPU9150_CMPS_ZOUT_H);
		data[4] = this.readSensor(MPU9150_GYRO_XOUT_L, MPU9150_GYRO_XOUT_H);
		data[5] = this.readSensor(MPU9150_GYRO_YOUT_L, MPU9150_GYRO_YOUT_H);
		data[6] = this.readSensor(MPU9150_GYRO_ZOUT_L, MPU9150_GYRO_ZOUT_H);
		data[7] = this.readSensor(MPU9150_ACCEL_XOUT_L, MPU9150_ACCEL_XOUT_H);
		data[8] = this.readSensor(MPU9150_ACCEL_YOUT_L, MPU9150_ACCEL_YOUT_H);
		data[9] = this.readSensor(MPU9150_ACCEL_ZOUT_L, MPU9150_ACCEL_ZOUT_H);
		return data;
	}
	
	public void init() {
		// Extremely modified from http://playground.arduino.cc/Main/MPU-9150
		// Mag setup
		compass.write(0x0A, 0x00); // PowerDownMode
		compass.write(0x0A, 0x0F); // SelfTest
		compass.write(0x0A, 0x00); // PowerDownMode
		// Gyro setup
		accelMag.write(0x24, 0x40); // Wait for Data at Slave0
		accelMag.write(0x25, 0x8C); // Set i2c address at slave0 at 0x0C
		accelMag.write(0x26, 0x02); // Set where reading at slave 0 starts
		accelMag.write(0x27, 0x88); // set offset at start reading and enable
		accelMag.write(0x28, 0x0C); // set i2c address at slv1 at 0x0C
		accelMag.write(0x29, 0x0A); // Set where reading at slave 1 starts
		accelMag.write(0x2A, 0x81); // Enable at set length to 1
		accelMag.write(0x64, 0x01); // overvride register
		accelMag.write(0x67, 0x03); // set delay rate
		accelMag.write(0x01, 0x80);
		accelMag.write(0x34, 0x04); // set i2c slv4 delay
		accelMag.write(0x64, 0x00); // override register
		accelMag.write(0x6A, 0x00); // clear usr setting
		accelMag.write(0x64, 0x01); // override register
		accelMag.write(0x6A, 0x20); // enable master i2c mode
		accelMag.write(0x34, 0x13); // disable slv4
	}
	
	public boolean test() {
		byte[] data = new byte[1];
		data[0] = (byte) 0x68;
		return accelMag.verifySensor(0x75, 1, data);
	}

	public void readAcc() {}

	// //////////////////////////
	// Low level I2C functions //
	// //////////////////////////
	private int readSensor(int addrL, int addrH) {
		byte[] H = new byte[1];
		byte[] L = new byte[1];
		if (!accelMag.read(addrH, 1, H)) {
			return -1;
		}
		if (!accelMag.read(addrL, 1, L)) {
			return -1;
		}
		return (H[0] << 8) + L[0];
	}
}
