package org.usfirst.frc4904.robot.input;


// We are combining multiple serial streams, this processes those.
import java.util.ArrayList;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;

public class SuperSerial implements Updatable {
	private final SerialPort port;
	ArrayList<Byte> lidarData = new ArrayList<Byte>();
	ArrayList<Byte> udarData = new ArrayList<Byte>();
	ArrayList<Byte> imuData = new ArrayList<Byte>();
	ArrayList<Byte>[] encoderData;
	
	public SuperSerial() {
		port = new SerialPort(115200, SerialPort.Port.kMXP);
		for (int i = 0; i < 5; i++) {
			encoderData[i] = new ArrayList<Byte>();
		}
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
				for (Byte aByte : line.getBytes()) {
					lidarData.add(aByte);
				}
			} else if (line.startsWith("UDAR")) {
				line = line.substring(4);
				for (Byte aByte : line.getBytes()) {
					udarData.add(aByte);
				}
			} else if (line.startsWith("IMU")) {
				line = line.substring(3);
				for (Byte aByte : line.getBytes()) {
					imuData.add(aByte);
				}
			} else if (line.startsWith("E0")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData[0].add(aByte);
				}
			} else if (line.startsWith("E1")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData[1].add(aByte);
				}
			} else if (line.startsWith("E2")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData[2].add(aByte);
				}
			} else if (line.startsWith("E3")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData[3].add(aByte);
				}
			} else if (line.startsWith("E4")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData[4].add(aByte);
				}
			}
		}
	}
	
	public byte[] readLIDAR(int bytes) {
		byte[] wantedData = new byte[bytes];
		for (int i = 0; i < bytes; i++) {
			wantedData[i] = lidarData.get(0);
			lidarData.remove(0);
		}
		return wantedData;
	}
	
	public int availableLIDARBytes() {
		return lidarData.size();
	}
	
	public byte[] readUDAR(int bytes) {
		byte[] wantedData = new byte[bytes];
		for (int i = 0; i < bytes; i++) {
			wantedData[i] = udarData.get(0);
			udarData.remove(0);
		}
		return wantedData;
	}
	
	public int availableUDARBytes() {
		return udarData.size();
	}
	
	public byte[] readEncoder(int encoder, int bytes) {
		byte[] wantedData = new byte[bytes];
		for (int i = 0; i < bytes; i++) {
			wantedData[i] = encoderData[encoder].get(0);
			encoderData[encoder].remove(0);
		}
		return wantedData;
	}
	
	public int availableEncoderBytes(int channel) {
		return encoderData[channel].size();
	}
	
	public byte[] readIMU(int bytes) {
		byte[] wantedData = new byte[bytes];
		for (int i = 0; i < bytes; i++) {
			wantedData[i] = imuData.get(0);
			imuData.remove(0);
		}
		return wantedData;
	}
	
	public int availableIMUBytes() {
		return imuData.size();
	}
}
