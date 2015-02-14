package org.usfirst.frc.team4904.robot.input;


import java.util.Comparator;
import java.util.Vector;
import org.usfirst.frc.team4904.robot.Disablable;
import org.usfirst.frc.team4904.robot.Updatable;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import edu.wpi.first.wpilibj.CameraServer;

public class Camera implements Updatable, Disablable {
	private CameraServer server;
	private int cameraSession;
	private Image frame;
	private boolean enabled;
	private double[] toteCoord = new double[2];
	private final NIVision.Range TOTE_HUE_RANGE = new NIVision.Range(101, 64); // Default hue range for yellow tote
	private final NIVision.Range TOTE_SAT_RANGE = new NIVision.Range(88, 255); // Default saturation range for yellow tote
	private final NIVision.Range TOTE_VAL_RANGE = new NIVision.Range(134, 255); // Default value range for yellow tote
	private static final double AREA_MINIMUM = 0.5;
	private static double LONG_RATIO = 2.22; // Tote long side = 26.9 / Tote height = 12.1 = 2.22
	private static double SHORT_RATIO = 1.4; // Tote short side = 16.9 / Tote height = 12.1 = 1.4
	private static double SCORE_MIN = 75.0; // Minimum score to be considered a tote
	private static final String CAMERA_NAME = "cam1";
	private NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	private NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0, 0, 1, 1);
	
	public Camera() {
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		cameraSession = NIVision.IMAQdxOpenCamera(CAMERA_NAME, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(cameraSession);
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture(CAMERA_NAME);
		enabled = false;
		criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);
	}
	
	private void enable() {
		enabled = true;
		NIVision.IMAQdxStartAcquisition(cameraSession);
	}
	
	public void disable() {
		enabled = false;
		NIVision.IMAQdxStopAcquisition(cameraSession);
	}
	
	public void update() {
		if (!enabled) {
			enable();
		}
		NIVision.IMAQdxGrab(cameraSession, frame, 1);
		server.setImage(frame);
		// Create simple image
		Image binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		// Threshold image
		NIVision.imaqColorThreshold(binaryFrame, frame, 255, NIVision.ColorMode.HSV, TOTE_HUE_RANGE, TOTE_SAT_RANGE, TOTE_VAL_RANGE);
		// Get yellow (thresholed) particle count
		int imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, filterOptions, null);
		int numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
		// If there is no yellow, there is no tote
		if (numParticles <= 0) {
			return;
		}
		// Find the biggest tote!
		Vector<ParticleReport> particles = new Vector<ParticleReport>();
		for (int pI = 0; pI < numParticles; pI++) {
			ParticleReport par = new ParticleReport();
			par.PercentAreaToImageArea = NIVision.imaqMeasureParticle(binaryFrame, pI, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
			par.Area = NIVision.imaqMeasureParticle(binaryFrame, pI, 0, NIVision.MeasurementType.MT_AREA);
			par.BoundingRectTop = NIVision.imaqMeasureParticle(binaryFrame, pI, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
			par.BoundingRectLeft = NIVision.imaqMeasureParticle(binaryFrame, pI, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
			par.BoundingRectBottom = NIVision.imaqMeasureParticle(binaryFrame, pI, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
			par.BoundingRectRight = NIVision.imaqMeasureParticle(binaryFrame, pI, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);
			particles.add(par);
		}
		particles.sort(null);// I find it hard to believe that this will work in any way
		ParticleReport tote = particles.get(0);
		// TODO actually get center
	}
	
	public double[] getYellowTote() { // This function will return the x, y coordinates of the center of the yellow tote.
		return new double[] {0, 0}; // It uses magic.
	}
	
	// Comparator function for sorting particles. Returns true if particle 1 is larger
	private boolean CompareParticleSizes(ParticleReport particle1, ParticleReport particle2) {
		// we want descending sort order
		return particle1.PercentAreaToImageArea > particle2.PercentAreaToImageArea;
	}
	
	// Method to score if the aspect ratio of the particle appears to match the retro-reflective target. Target is 7"x7" so aspect should be 1
	private double AspectScore(ParticleReport report) {
		return ratioToScore(((report.BoundingRectRight - report.BoundingRectLeft) / (report.BoundingRectBottom - report.BoundingRectTop)));
	}
	
	private double ratioToScore(double ratio) {
		return (Math.max(0, Math.min(100 * (1 - Math.abs(1 - ratio)), 100)));
	}
	
	// A structure to hold measurements of a particle
	private class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport> {
		double PercentAreaToImageArea;
		double Area;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;
		
		public int compareTo(ParticleReport r) {
			return (int) (r.Area - Area);
		}
		
		public int compare(ParticleReport r1, ParticleReport r2) {
			return (int) (r1.Area - r2.Area);
		}
	};
}
