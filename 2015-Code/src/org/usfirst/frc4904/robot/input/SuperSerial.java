package org.usfirst.frc4904.robot.input;


// We are combining multiple serial streams, this processes those.
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class SuperSerial implements Updatable {
	private final SerialPort port;
	private volatile String lidarData;
	private volatile String udarData;
	private volatile String imuData;
	private volatile String[] encoderData = new String[5];
	private static final int NUM_LEDS = 209;
	private final LogKitten logger;
	
	public SuperSerial() {
		port = new SerialPort(115200, SerialPort.Port.kMXP);
		lidarData = "";
		udarData = "";
		imuData = "";
		for (int i = 0; i < 5; i++) {
			encoderData[i] = "";
		}
		logger = new LogKitten("SuperSerial", LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_VERBOSE);
	}
	
	public void update() {
		int available = port.getBytesReceived();
		logger.v("update", available + " bytes received");
		while (port.getBytesReceived() > 50) {
			String data = "";
			String current = "a";
			while (!current.matches("\n")) {
				current = port.readString(1);
				data += current;
			}
			logger.v("update", "Got data " + data);
			imuData = data;
			/*
			 * logger.v("update", port.getBytesReceived() + " bytes received"); logger.v("update", "Got data " + data); String[] lines = data.split("\n"); for (String line : lines) { logger.v("line", line); line = line.substring(0, line.length() - 1); if (line.startsWith("LIDAR")) { line = line.substring(5); lidarData += "#" + line + "$"; } else if (line.startsWith("UDAR")) { line = line.substring(4); udarData += "#" + line + "$"; } else if (line.startsWith("IMU")) { line = line.substring(3); logger.v("imuData", imuData); imuData = line; logger.v("Update", "Added " + line + " to IMU"); } else if (line.startsWith("E0")) { line = line.substring(2); encoderData[0] += "#" + line + "$"; } else if (line.startsWith("E1")) { line = line.substring(2); encoderData[1] += "#" + line + "$"; } else if (line.startsWith("E2")) { line = line.substring(2); encoderData[2] += "#" + line + "$"; } else if (line.startsWith("E3")) { line = line.substring(2); encoderData[3] += "#" + line + "$"; } else if (line.startsWith("E4")) { line = line.substring(2); encoderData[4] += "#" + line + "$"; } }
			 */
		}
	}
	
	public String readLIDAR() { // Reads the first available full LIDAR pulse
		String data = "";
		int iter = 0;
		while (lidarData.charAt(iter) != '#') {
			iter++;
		}
		while (lidarData.charAt(iter) != '$') {
			data += lidarData.charAt(iter);
		}
		lidarData = lidarData.substring(iter, lidarData.length());
		return data;
	}
	
	public int availableLIDARData() {
		return lidarData.length();
	}
	
	public String readUDAR() { // Reads the first available full UDAR pulse
		String data = "";
		int iter = 0;
		while (udarData.charAt(iter) != '#') {
			iter++;
		}
		while (udarData.charAt(iter) != '$') {
			data += udarData.charAt(iter);
		}
		udarData = udarData.substring(iter, udarData.length());
		return data;
	}
	
	public int availableUDARData() {
		return udarData.length();
	}
	
	public String readEncoder(int encoder) { // Reads the first available full encoder pulse
		String data = "";
		int iter = 0;
		while (encoderData[encoder].charAt(iter) != '#') {
			iter++;
		}
		while (encoderData[encoder].charAt(iter) != '$') {
			data += encoderData[encoder].charAt(iter);
		}
		encoderData[encoder] = encoderData[encoder].substring(iter, imuData.length());
		return data;
	}
	
	public int availableEncoderData(int channel) {
		return encoderData[channel].length();
	}
	
	public String readIMU() { // Reads the first available full IMU pulse
		String data = "";
		int iter = 0;
		while (imuData.charAt(iter) != '#') {
			iter++;
		}
		while (imuData.charAt(iter) != '$') {
			data += imuData.charAt(iter);
		}
		imuData = imuData.substring(iter, imuData.length());
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
