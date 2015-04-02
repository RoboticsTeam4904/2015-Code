package org.usfirst.frc4904.robot;


public class TimeSafeguard extends Safeguard {
	private final int duration;
	private long start;
	
	public TimeSafeguard(String name, int duration) {
		super(name);
		this.duration = duration * 1000; // seconds to milliseconds
		reset();
	}
	
	public void reset() {
		start = System.currentTimeMillis();
	}
	
	protected boolean safetyCondition() {
		if (System.currentTimeMillis() - start > duration) {
			reset();
			return false;
		}
		return true;
	}
}
