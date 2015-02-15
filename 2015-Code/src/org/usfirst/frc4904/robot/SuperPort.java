package org.usfirst.frc4904.robot;


public abstract class SuperPort implements Updatable {
	public double[] data;
	
	public double[] read() {
		return data;
	}
}
