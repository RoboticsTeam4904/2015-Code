package org.usfirst.frc4904.robot.autonomous;


import org.usfirst.frc4904.robot.autonomous.autosteps.WaitWithMessage;

public class Freeze extends Autonomous {
	Freeze() {
		super("Freeze", new Step[] {new WaitWithMessage(5, "You know what is cooler than cool?"), new WaitWithMessage(5, "An iceberg")});
	}
}