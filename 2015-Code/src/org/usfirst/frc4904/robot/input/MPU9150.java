package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import edu.wpi.first.wpilibj.SerialPort;

public class MPU9150 {
	private SerialPort port;
	private double[] angles;
	
	public MPU9150() {
		port = new SerialPort(115200, SerialPort.Port.kMXP);
		port.flush();
		angles = new double[3];
		Arrays.fill(angles, 0);
	}
	
	public double[] readRaw() {
		return angles;
	}
	
	public float parseFloat(String data) {
		try {
			data = data.substring(2, 10); // discard the leading "f:"
			float quad = Float.intBitsToFloat(Integer.reverseBytes(Integer.parseInt(data, 16)));
			return quad;
		}
		catch (Exception e) {
			// e.printStackTrace();
			return 0;
		}
	}
	
	public void update() {
		byte[] buffer = new byte[1];
		buffer[0] = 'e';
		port.write(buffer, 1);
		String returnData = port.readString();
		port.flush();
		String[] floatString = returnData.split(",");
		if (floatString.length > 3) {
			double q[] = new double[4];
			q[3] = parseFloat(floatString[0]);
			q[2] = parseFloat(floatString[1]);
			q[1] = parseFloat(floatString[2]);
			q[0] = parseFloat(floatString[3]);
			angles[0] = -1 * Math.atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0] * q[0] + 2 * q[1] * q[1] - 1) * (180 / Math.PI); // psi
			angles[1] = -1 * Math.asin(2 * q[1] * q[3] + 2 * q[0] * q[2]) * 180 / Math.PI; // theta
			angles[2] = Math.atan2(2 * q[2] * q[3] - 2 * q[0] * q[1], 2 * q[0] * q[0] + 2 * q[3] * q[3] - 1) * 180 / Math.PI; // phi
		}
	}
}
