package org.usfirst.frc4904.robot.input;


import java.util.Comparator;
import java.util.Vector;
import org.usfirst.frc4904.robot.Updatable;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

public class Camera implements Updatable {
	private int cameraSession;
	private Image frame;
	private double[] toteCoord = new double[2];
	private final NIVision.Range TOTE_HUE_RANGE = new NIVision.Range(101, 64); // Default hue range for yellow tote
	private final NIVision.Range TOTE_SAT_RANGE = new NIVision.Range(88, 255); // Default saturation range for yellow tote
	private final NIVision.Range TOTE_VAL_RANGE = new NIVision.Range(134, 255); // Default value range for yellow tote
	private static final double AREA_MINIMUM = 0.5;
	private static final double LONG_RATIO = 2.22; // Tote long side = 26.9 / Tote height = 12.1 = 2.22
	private static final double SHORT_RATIO = 1.4; // Tote short side = 16.9 / Tote height = 12.1 = 1.4
	private static final double SCORE_MIN = 75.0; // Minimum score to be considered a tote
	private static int CAMERA_QUALITY = 10;
	private double X_RES;
	private double Y_RES;
	private static final String CAMERA_NAME = "cam1";
	private NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	private NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0, 0, 1, 1);
	private final boolean enabled;
	
	public Camera(boolean enabled) {
		this.enabled = enabled;
		if (!enabled) {
			return;
		}
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		cameraSession = NIVision.IMAQdxOpenCamera(CAMERA_NAME, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(cameraSession);
		criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);
	}
	
	public void update() {
		if (!enabled) {
			return;
		}
		NIVision.IMAQdxGrab(cameraSession, frame, 1);
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
		particles.sort(null);// I find it easy to believe that this will work in any way
		ParticleReport tote = particles.get(0);
		NIVision.GetImageSizeResult size = NIVision.imaqGetImageSize(frame);
		X_RES = size.width;
		Y_RES = size.height;
		// Actually get center
		toteCoord[0] = (tote.BoundingRectRight + tote.BoundingRectLeft) / 2;
		toteCoord[1] = (tote.BoundingRectTop + tote.BoundingRectBottom) / 2;
	}
	
	public double[] getYellowTote() { // This function will return the scaled x, y coordinates of the center of the yellow tote.
		double[] scaledCoord = new double[2];
		scaledCoord[0] = (toteCoord[0] - (X_RES / 2)) / X_RES;
		scaledCoord[1] = (toteCoord[1] - (Y_RES / 2)) / Y_RES;
		return scaledCoord; // It uses logic.
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
