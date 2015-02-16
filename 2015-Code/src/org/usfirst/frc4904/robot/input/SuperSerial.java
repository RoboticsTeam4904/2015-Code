package org.usfirst.frc4904.robot.input;


// We are combining multiple serial streams, this processes those.
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class SuperSerial implements Updatable {
	private final SerialPort port;
	private byte[] lidarData = new byte[22];
	private byte[] encoderData = new byte[5];
	
	public SuperSerial() {
		port = new SerialPort(115200, SerialPort.Port.kMXP);
	}
	
	public void update() {
		int available = port.getBytesReceived();
		byte[] data = new byte[available];
		data = port.read(available);
		// Parse data
		for (int i = 0; i < 22; i++) {
			lidarData[i] = data[i];
		}
		for (int i = 22; i < 26; i++) {
			encoderData[i - 22] = data[i];
		}
	}
	
	public byte[] readLIDAR() {
		return lidarData;
	}
	
	public byte[] readEncoder(int encoder) {
		return new byte[] {encoderData[encoder]};
	}
}
