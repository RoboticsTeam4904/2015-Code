package org.usfirst.frc.team4904.robot.input;


import java.math.BigInteger;
import org.usfirst.frc.team4904.robot.Updatable;
import org.usfirst.frc.team4904.robot.output.LIDARMotor;

public class LIDAR implements Updatable {
	int[] Dists = new int[360];
	LIDARMotor motor;
	
	public LIDAR(int motorport) {
		motor = new LIDARMotor(6);
	}
	
	public int connect() {
		// TODO add RS-232 connect
		return 0;
	}
	
	private byte[] read(int bytes) throws Exception {
		// TODO RS-232 read
		byte b[] = new byte[bytes];
		return b;
	}
	
	private int[] scanline_b(byte angle) throws Exception {
		boolean insync = false;
		while (!insync) { // Wait until beginning of distance data
			byte header = read(1)[0]; // Header is one byte
			if (header == (byte) 0xFA) {
				byte scan = read(1)[0];
				if (scan == angle) insync = true;
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
		return Dists;
	}
	
	public int bytesCurrentlyAvailable() {
		return 0;
	}
	
	public void update() {
		if (bytesCurrentlyAvailable() < 1000) {
			return;
		}
		byte scanhdr = (byte) 0xA0;
		try {
			for (int i = 0; i < 90; i++) { // Reading in chunks of 4, so only 90 steps
				int[] scanrange = scanline_b(scanhdr);
				int degree = 4 * ((int) (scanhdr - ((byte) 0xA0)));
				if (scanrange != null) {
					for (int j = 0; j < 4; j++) {
						if (scanrange[j] != 53) Dists[degree + j] = scanrange[0]; // No one knows why we are comparing scanrange[j] to 53, so I am too scared to change it
						else Dists[degree + j] = 0;
					}
				}
				if (scanhdr == (byte) 0xF9) scanhdr = (byte) 0xA0;
				else scanhdr += 1;
			}
			for (int i = 0; i < 90; i++) { // Do it again for redundancy
				int[] scanrange = scanline_b(scanhdr);
				int degree = 4 * ((int) (scanhdr - ((byte) 0xA0)));
				if (scanrange != null) {
					for (int j = 0; j < 4; j++) {
						if (scanrange[j] != 53) Dists[degree + j] = scanrange[0];
						else Dists[degree + j] = 0;
					}
				}
				if (scanhdr == (byte) 0xF9) scanhdr = (byte) 0xA0;
				else scanhdr += 1;
			}
		}
		catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	
	public int clean() {
		// TODO add RS-232 cleanup port
		return 0;
	}
}