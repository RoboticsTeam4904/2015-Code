package org.usfirst.frc4904.robot;


public interface Overridable<T> {
	public void override(T value);
	
	public void stopOverride();
}
