package org.usfirst.frc4904.robot.input;


// We are combining multiple serial streams, this processes those.
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class SuperSerial implements Updatable {
	private final SerialPort port;
	private volatile String lidarData;
	private volatile String udarData;
	private volatile String imuData;
	private volatile String[] encoderData = new String[5];
	private static final int NUM_LEDS = 209;
	
	public SuperSerial() {
		port = new SerialPort(230400, SerialPort.Port.kMXP);
	}
	
	public void update() {
		int available = port.getBytesReceived();
		if (available < 20) {
			return;
		}
		String data = port.readString();
		String[] lines = data.split("\n");
		for (String line : lines) {
			if (line.startsWith("LIDAR")) {
				line = line.substring(5);
				lidarData += "#" + line + "$";
			} else if (line.startsWith("UDAR")) {
				line = line.substring(4);
				udarData += "#" + line + "$";
			} else if (line.startsWith("IMU")) {
				line = line.substring(3);
				imuData += "#" + line + "$";
			} else if (line.startsWith("E0")) {
				line = line.substring(2);
				encoderData[0] += "#" + line + "$";
			} else if (line.startsWith("E1")) {
				line = line.substring(2);
				encoderData[1] += "#" + line + "$";
			} else if (line.startsWith("E2")) {
				line = line.substring(2);
				encoderData[2] += "#" + line + "$";
			} else if (line.startsWith("E3")) {
				line = line.substring(2);
				encoderData[3] += "#" + line + "$";
			} else if (line.startsWith("E4")) {
				line = line.substring(2);
				encoderData[4] += "#" + line + "$";
			}
		}
	}
	
	public String readLIDAR(int length) {
		String data = new String();
		data = lidarData.substring(0, length);
		lidarData = lidarData.substring(length);
		return data;
	}
	
	public int availableLIDARData() {
		return lidarData.length();
	}
	
	public String readUDAR(int length) {
		String data = new String();
		data = udarData.substring(0, length);
		lidarData = udarData.substring(length);
		return data;
	}
	
	public int availableUDARData() {
		return udarData.length();
	}
	
	public String readEncoder(int encoder, int length) {
		String data = new String();
		data = encoderData[encoder].substring(0, length);
		lidarData = encoderData[encoder].substring(length);
		return data;
	}
	
	public int availableEncoderData(int channel) {
		return encoderData[channel].length();
	}
	
	public String readIMU(int length) {
		String data = new String();
		data = imuData.substring(0, length);
		lidarData = imuData.substring(length);
		return data;
	}
	
	public int availableIMUData() {
		return imuData.length();
	}
	
	public void setLeds(byte[] leds) {
		port.writeString("a");
		try {
			port.write(leds, 627);
		}
		catch (Exception e) {
			// DIE, you person who put an invalid length of leds
		}
	}
}
