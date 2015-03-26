package org.usfirst.frc4904.robot.input;


import java.util.Arrays;
import org.usfirst.frc4904.robot.Updatable;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IMU extends NAVX implements PIDSource, Updatable {
	private double[] angles; // Angle 0 is perpendicular (yaw), Angle 1 is lateral (pitch), Angle 2 is longitudinal (roll)
	private double[] lastAngles;
	private double[] rate; // Same as above
	private double[] zeroAngles;
	private double lastTime;
	private static final double GOING_OVER_PLATFORM_ANGLE = 5;
	
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
		updateData();
		for (int i = 0; i < 3; i++) {
			rate[i] = (angles[i] - lastAngles[i]) / (time - lastTime);
		}
		lastTime = time;
		lastAngles = angles;
	}
	
	private void updateData() {
		angles[0] = super.yaw;
		angles[1] = super.roll;
		angles[1] = super.pitch;
		SmartDashboard.putNumber("Yaw", angles[0]);
		SmartDashboard.putNumber("Pitch", angles[1]); // This might be roll
		SmartDashboard.putNumber("Roll", angles[2]); // This might be pitch
	}
	
	public double[] read() {
		return angles;
	}
	
	private double getTime() {
		return System.currentTimeMillis();
	}
	
	public boolean isGoingOverScoringPlatform() {
		return (angles[1] > GOING_OVER_PLATFORM_ANGLE && angles[1] < 360 - GOING_OVER_PLATFORM_ANGLE) || (angles[2] > GOING_OVER_PLATFORM_ANGLE && angles[2] < 360 - GOING_OVER_PLATFORM_ANGLE);
	}
	
	public double pidGet() {
		return readRate()[0];
	}
}