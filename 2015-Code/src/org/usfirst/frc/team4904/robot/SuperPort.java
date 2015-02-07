package org.usfirst.frc.team4904.robot;


public abstract class SuperPort implements Updatable {
	public double[] data;
	
	public double[] read() {
		return data;
	}
}
