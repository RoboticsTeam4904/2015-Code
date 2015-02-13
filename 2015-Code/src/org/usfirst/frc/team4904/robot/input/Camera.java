package org.usfirst.frc.team4904.robot.input;


import edu.wpi.first.wpilibj.CameraServer;

public class Camera {
	private CameraServer server;
	
	public Camera() {
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam0");
	}
	
	public double[] getYellowTote() { // This function will return the x, y coordinates of the center of the yellow tote.
		return new double[] {0, 0}; // It uses magic.
	}
}
