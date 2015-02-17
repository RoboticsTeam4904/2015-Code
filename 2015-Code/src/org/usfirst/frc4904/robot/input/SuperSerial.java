package org.usfirst.frc4904.robot.input;


// We are combining multiple serial streams, this processes those.
import java.util.ArrayList;
import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class SuperSerial implements Updatable {
	private final SerialPort port;
	private volatile ArrayList<String> imuData;
	private static final int NUM_LEDS = 209;
	private final LogKitten logger;
	
	public SuperSerial() {
		port = new SerialPort(115200, SerialPort.Port.kMXP);
		imuData = new ArrayList<String>();
		logger = new LogKitten("SuperSerial", LogKitten.LEVEL_DEBUG, LogKitten.LEVEL_VERBOSE);
	}
	
	public void update() {
		String data = "";
		String current = "a";
		while (!current.matches("\n")) {
			current = port.readString(1);
			data += current;
		}
		logger.v("update", "Got data " + data);
		imuData.add(data);
	}
	
	public String readIMU() { // Reads the first available full IMU pulse
		String data = imuData.get(0);
		imuData.remove(0);
		return data;
	}
	
	public int availableIMUData() {
		return imuData.size();
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
