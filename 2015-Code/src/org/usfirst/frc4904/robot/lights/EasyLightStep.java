package org.usfirst.frc4904.robot.lights;


public class EasyLightStep {
	protected volatile int redValue;
	protected volatile int blueValue;
	protected volatile int greenValue;
	protected volatile long duration;
	private volatile long completeTime;
	private volatile boolean lightsAreSet;
	private int ledCount = 209;
	private byte[] ledData = new byte[ledCount * 3]; // length of 209
	
	public class RGBRange {
		public int r;
		public int g;
		public int b;
		public int startIndex;
		public int endIndex;
		public long durationMillis;
		
		public RGBRange(int r, int g, int b, double start, double end) {
			this.r = r;
			this.g = g;
			this.b = b;
			if (start < 1 && start > 0) { // handle percentages if given
				this.startIndex = (int) Math.floor(start * ledCount);
			} else {
				this.startIndex = (int) start;
			}
			if (end < 1 && end > 0) {
				this.startIndex = (int) Math.floor(start * ledCount);
			} else {
				this.startIndex = (int) start;
			}
		}
	}
	protected RGBRange[] ranges;
	protected final RGBRange resetRange = new RGBRange(0, 0, 0, 0, ledCount);
	
	private void init(long durationMillis) {
		completeTime = System.currentTimeMillis() + durationMillis;
		for (int i = ledData.length; i >= 0; i--) {
			ledData[i] = (byte) 0;
		}
	}
	
	public EasyLightStep(RGBRange[] ranges, long durationMillis) {
		init(durationMillis);
		this.ranges = ranges;
	}
	
	public EasyLightStep(int r, int g, int b, long durationMillis) {
		init(durationMillis);
		this.ranges[0] = new RGBRange(r, g, b, 0, ledCount);
	}
	
	public void applyRanges(RGBRange[] ranges) {
		for (RGBRange range : ranges) {
			applyRange(range);
		}
		lightsAreSet = true;
	}
	
	public void applyRange(RGBRange range) {
		if (range.startIndex < 0 || range.endIndex > ledCount) {
			return; // if range is impossible, skip setting the LEDs
		}
		for (int led = range.startIndex; led < range.endIndex; led++) {
			ledData[3 * led + 0] = (byte) range.r;
			ledData[3 * led + 1] = (byte) range.g;
			ledData[3 * led + 2] = (byte) range.b;
		}
	}
	
	public boolean run() {
		if (!lightsAreSet) {
			applyRanges(this.ranges);
		}
		if (System.currentTimeMillis() < completeTime) {
			return false;
		}
		resetLights();
		return true;
	}
	
	public void resetLights() {
		applyRange(this.resetRange);
	}
	
	public byte[] getLedData() {
		return ledData;
	}
}