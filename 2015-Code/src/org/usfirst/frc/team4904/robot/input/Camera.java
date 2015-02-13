package org.usfirst.frc.team4904.robot.input;


import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.Updatable;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.CameraServer;

public class Camera implements Updatable, Disablable {
	private CameraServer server;
	private int cameraSession;
	private Image frame;
	private boolean enabled;
	
	public Camera() {
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		cameraSession = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(cameraSession);
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam0");
		enabled = false;
	}
	
	private void enable() {
		NIVision.IMAQdxStartAcquisition(cameraSession);
	}
	
	public void disable() {
		NIVision.IMAQdxStopAcquisition(cameraSession);
	}
	
	public void update() {
		if (!enabled) {
			enable();
		}
		NIVision.IMAQdxGrab(cameraSession, frame, 1);
		server.setImage(frame);
	}
	
	public double[] getYellowTote() { // This function will return the x, y coordinates of the center of the yellow tote.
		return new double[] {0, 0}; // It uses magic.
	}
}
