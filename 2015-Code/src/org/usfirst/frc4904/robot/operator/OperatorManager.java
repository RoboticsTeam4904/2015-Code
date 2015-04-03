package org.usfirst.frc4904.robot.operator;


import org.usfirst.frc4904.robot.LogKitten;
import org.usfirst.frc4904.robot.TypedNamedSendableChooser;
import org.usfirst.frc4904.robot.input.LogitechJoystick;
import org.usfirst.frc4904.robot.output.Grabber;
import org.usfirst.frc4904.robot.output.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorManager extends TypedNamedSendableChooser<Operator> {
	private final LogKitten logger;
	
	public OperatorManager(LogitechJoystick stick, Winch winch, Grabber grabber) {
		super();
		addObject(new OperatorGriffin());
		addDefault(new OperatorNachi());
		Operator.passSensors(stick, winch, grabber);
		logger = new LogKitten("OperatorManager", LogKitten.LEVEL_VERBOSE);
		SmartDashboard.putData("Operator Chooser", this);
	}
}
