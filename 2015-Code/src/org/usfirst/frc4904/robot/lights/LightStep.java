package org.usfirst.frc4904.robot.lights;


public class LightStep {
	protected volatile int redValue;
	protected volatile int blueValue;
	protected volatile int greenValue;
	protected volatile long duration;
	private volatile long completeTime;
	private volatile boolean lightsAreSet;
	private LightManager lights;
	protected int ledIndexRange;
	
	protected class RGBRange {
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
				this.startIndex = (int) Math.floor(start * ledIndexRange);
			} else {
				this.startIndex = (int) start;
			}
			if (end < 1 && end > 0) {
				this.startIndex = (int) Math.floor(start * ledIndexRange);
			} else {
				this.startIndex = (int) start;
			}
		}
	}
	protected RGBRange[] ranges;
	protected final RGBRange resetRange = new RGBRange(0, 0, 0, 0, ledIndexRange);
	
	public LightStep(RGBRange[] ranges, int durationMillis) {
		completeTime = System.currentTimeMillis() + durationMillis;
		lights = new LightManager(4);
		ledIndexRange = lights.leds - 1;
		this.ranges = ranges;
	}
	
	public LightStep(int r, int g, int b, int durationMillis) {
		completeTime = System.currentTimeMillis() + durationMillis;
		lights = new LightManager(4);
		ledIndexRange = lights.leds - 1;
		this.ranges[0] = new RGBRange(r, g, b, 0, lights.leds);
	}
	
	public void applyRanges(RGBRange[] ranges) {
		for (RGBRange range : ranges) {
			applyRange(range);
		}
		lights.update();
		lightsAreSet = true;
	}
	
	public void applyRange(RGBRange range) {
		if (range.startIndex < 0 || range.endIndex > ledIndexRange) {
			return; // if range is impossible, skip setting the LEDs
		}
		for (int i = range.startIndex; i <= range.endIndex; i++) {
			lights.setLED(i, range.r, range.g, range.b);
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
		lights.update();
	}
}
