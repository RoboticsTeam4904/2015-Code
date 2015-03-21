package org.usfirst.frc4904.robot;


import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public abstract class TypedNamedSendableChooser<T extends Named> extends SendableChooser {
	public void addObject(T object) {
		super.addObject(object.getName(), object);
	}
	
	public void addDefault(T object) {
		super.addDefault(object.getName() + " (default)", object);
	}
	
	public void addObject(String arg0, Object arg1) {
		throw new Error("addObject on TypedNamedSendableChooser shouldn't be called directly!");
	}
	
	public void addDefault(String arg0, Object arg1) {
		throw new Error("addDefault on TypedNamedSendableChooser shouldn't be called directly!");
	}
	
	@SuppressWarnings("unchecked")
	public T getSelected() {
		return (T) super.getSelected();
	}
}
