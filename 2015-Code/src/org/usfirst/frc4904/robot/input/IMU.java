package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.PIDSource;

public class IMU extends MPU9150 implements PIDSource, Updatable {
	private double[] angles; // Angle 0 is perpendicular (yaw), Angle 1 is lateral (pitch), Angle 2 is longitudinal (roll)
	private double[] lastAngles;
	private double[] rate; // Same as above
	private double[] zeroAngles;
	private double lastTime;
	
	public IMU() {
		super();
		angles = new double[3];
		lastAngles = new double[3];
		zeroAngles = new double[3];
		rate = new double[3];
		Arrays.fill(angles, 0);
		Arrays.fill(lastAngles, 0);
		Arrays.fill(zeroAngles, 0);
		lastTime = getTime();
		zero();
	}
	
	public double[] readRate() {
		return rate;
	}
	
	public void zero() {
		update();
		zeroAngles[0] += angles[0];
		zeroAngles[1] += angles[1];
		zeroAngles[2] += angles[2];
	}
	
	public synchronized void update() {
		double time = getTime();
		super.update();
		updateData();
		for (int i = 0; i < 3; i++) {
			rate[i] = (angles[i] - lastAngles[i]) / (time - lastTime);
		}
		lastTime = time;
		lastAngles = angles;
	}
	
	private void updateData() {
		angles = super.readRaw();
		angles[0] = ((angles[0] - zeroAngles[0] + 3600) % 360);
		angles[1] = ((angles[1] - zeroAngles[1] + 3600) % 360);
		angles[2] = ((angles[2] - zeroAngles[2] + 3600) % 360);
	}
	
	public double[] read() {
		return angles;
	}
	
	private double getTime() {
		return System.currentTimeMillis();
	}
	
	public boolean isGoingOverScoringPlatform() {
		double[] eulers = read();
		double angle = eulers[1];
		return angle > 5 * Math.PI / 180; // TODO 5 degrees is ballpark - measure
	}
	
	public double pidGet() {
		return read()[0];
	}
}