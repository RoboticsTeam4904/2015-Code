package org.usfirst.frc.team4904.robot;


public abstract class Port implements Updatable {
	public double[] data;
	
	public double[] read() {
		return data;
	}
}
