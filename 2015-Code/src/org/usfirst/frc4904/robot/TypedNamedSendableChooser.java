package org.usfirst.frc4904.robot;


import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public abstract class TypedNamedSendableChooser<T extends Named> extends SendableChooser {
	public void addObject(T object) {
		super.addObject(object.getName(), object);
	}
	
	public void addDefault(T object) {
		super.addDefault(object.getName() + " (default)", object);
	}
	
	@SuppressWarnings("unchecked")
	public T getSelected() {
		return (T) super.getSelected();
	}
}
