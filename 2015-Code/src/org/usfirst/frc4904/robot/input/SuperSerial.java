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
	ArrayList<Byte> encoderData0 = new ArrayList<Byte>();
	ArrayList<Byte> encoderData1 = new ArrayList<Byte>();
	ArrayList<Byte> encoderData2 = new ArrayList<Byte>();
	ArrayList<Byte> encoderData3 = new ArrayList<Byte>();
	ArrayList<Byte> encoderData4 = new ArrayList<Byte>();
	
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
					encoderData0.add(aByte);
				}
			} else if (line.startsWith("E1")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData1.add(aByte);
				}
			} else if (line.startsWith("E2")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData2.add(aByte);
				}
			} else if (line.startsWith("E3")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData3.add(aByte);
				}
			} else if (line.startsWith("E4")) {
				line = line.substring(2);
				for (Byte aByte : line.getBytes()) {
					encoderData4.add(aByte);
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
			switch (encoder) {
				case 0:
					wantedData[i] = encoderData0.get(0);
					encoderData0.remove(0);
				case 1:
					wantedData[i] = encoderData1.get(0);
					encoderData1.remove(0);
				case 2:
					wantedData[i] = encoderData2.get(0);
					encoderData2.remove(0);
				case 3:
					wantedData[i] = encoderData3.get(0);
					encoderData3.remove(0);
				case 4:
					wantedData[i] = encoderData4.get(0);
					encoderData4.remove(0);
				default:
					wantedData[i] = 0x00;
			}
		}
		return wantedData;
	}
	
	public int availableEncoderBytes(int channel) {
		switch (channel) {
			case 0:
				return encoderData0.size();
			case 1:
				return encoderData1.size();
			case 2:
				return encoderData2.size();
			case 3:
				return encoderData3.size();
			case 4:
				return encoderData4.size();
			default:
				return 0;
		}
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
