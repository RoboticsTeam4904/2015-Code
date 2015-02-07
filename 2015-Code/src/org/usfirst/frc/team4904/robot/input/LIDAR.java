package org.usfirst.frc.team4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.LogKitten;
import org.usfirst.frc.team4904.robot.Updatable;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Talon;

public class LIDAR implements Disablable, Updatable {
	int[] dists = new int[360];
	private final Talon motor;
	private SerialPort port;
	private final LogKitten logger;
	
	public LIDAR(int motorport) {
		motor = new Talon(motorport);
		// port = new SerialPort(9600, SerialPort.Port.kOnboard);
		logger = new LogKitten("LIDAR", LogKitten.LEVEL_DEBUG);
	}
	
	private byte[] read(int bytes) throws Exception {
		byte[] b = new byte[bytes];
		// b = port.read(bytes);
		for (int i = 0; i < bytes; i++) {
			logger.d("read", Integer.toString(i) + " " + Byte.toString(b[i]));
		}
		return b;
	}
	
	private int[] scanline_b(byte angle) throws Exception {
		boolean insync = false;
		while (!insync) { // Wait until beginning of distance data
			byte header = read(1)[0]; // Header is one byte
			if (header == (byte) 0xFA) {
				byte scan = read(1)[0];
				if (scan == angle) {
					insync = true;
				}
			}
		}
		if (insync) {
			byte[][] b_data = new byte[4][]; // Read four values at a time
			for (int i = 0; i < 4; i++) {
				b_data[i] = read(4);
			}
			int[] dist = new int[4];
			for (int i = 0; i < 4; i++) {
				b_data[i][1] &= 0x3F;
				dist[i] = new BigInteger(new byte[] {0, b_data[i][1], b_data[i][0]}).intValue();
			}
			return dist;
		}
		return null;
	}
	
	public int[] getDists() {
		return dists;
	}
	
	public int[] getLine() {
		double[] xs = new double[180];
		double[] ys = new double[180];
		for (int i = 0; i < 180; i++) {
			xs[i] = dists[i] * Math.cos((double) i);
			ys[i] = dists[i] * Math.sin((double) i);
		}
		double angle = Math.atan2(ys[90], xs[90]);
		int startIter = 90;
		int endIter = 90;
		for (int i = 0; i < 180; i++) {
			double thisAngle = Math.atan2(ys[i], xs[i]);
			if (Math.abs(thisAngle - angle) < 5) {
				if (i < startIter) {
					startIter = i;
				} else if (i > endIter) {
					endIter = i;
				}
			}
		}
		int[] line = new int[4];
		line[0] = (int) xs[startIter];
		line[1] = (int) ys[startIter];
		line[2] = (int) xs[endIter];
		line[3] = (int) ys[endIter];
		return line;
	}
	
	private int bytesCurrentlyAvailable() {
		return 0;
		// return port.getBytesReceived();
	}
	
	public void update() {
		motor.set(0.5);
		if (bytesCurrentlyAvailable() < 10000) {
			return;
		}
		System.out.println("Reading from LIDAR");
		byte scanhdr = (byte) 0xA0;
		try {
			for (int i = 0; i < 90; i++) { // Reading in chunks of 4, so only 90 steps
				int[] scanrange = scanline_b(scanhdr);
				int degree = 4 * (scanhdr - (byte) 0xA0);
				if (scanrange != null) {
					for (int j = 0; j < 4; j++) {
						if (scanrange[j] != 53) {
							dists[degree + j] = scanrange[0]; // No one knows why we are comparing scanrange[j] to 53, so I am too scared to change it
						} else {
							dists[degree + j] = 0;
						}
					}
				}
				if (scanhdr == (byte) 0xF9) {
					scanhdr = (byte) 0xA0;
				} else {
					scanhdr += 1;
				}
			}
			for (int i = 0; i < 90; i++) { // Do it again for redundancy
				int[] scanrange = scanline_b(scanhdr);
				int degree = 4 * (scanhdr - (byte) 0xA0);
				if (scanrange != null) {
					for (int j = 0; j < 4; j++) {
						if (scanrange[j] != 53) {
							dists[degree + j] = scanrange[0];
						}
					}
				}
				if (scanhdr == (byte) 0xF9) {
					scanhdr = (byte) 0xA0;
				} else {
					scanhdr += 1;
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	
	public void disable() {
		motor.set(0);
	}
	
	public int clean() {
		// port.free();
		return 0;
	}
}