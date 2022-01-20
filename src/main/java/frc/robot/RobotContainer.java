// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;
import org.usfirst.frc3620.misc.CANDeviceType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  public final static Logger logger = EventLogging.getLogger(RobotContainer.class, Level.INFO);
  
  // need this
  static CANDeviceFinder canDeviceFinder;

  // The robot's subsystems and commands are defined here...
  static ShooterSubsystem shooterSubsystem;

  static TalonFX top1, top2, bottom;

  DigitalInput daButton;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    canDeviceFinder = new CANDeviceFinder();
    logger.info ("CAN bus: " + canDeviceFinder.getDeviceSet());

    makeHardware();
    makeSubsystems();

    // Configure the button bindings
    configureButtonBindings();
  }

  void makeHardware() {
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 1, "top motor 1")) {
      top1 = new TalonFX(1);
    }
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 2, "top motor 2")) {
      top2 = new TalonFX(2);
    }
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 3, "bottom motor")) {
      bottom = new TalonFX(3);
    }
    daButton = new DigitalInput(9);
  }

  void makeSubsystems() {
    shooterSubsystem = new ShooterSubsystem(top1, top2, bottom);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //Joystick driverJoystick = new Joystick(0);
    // new JoystickButton(driverJoystick, XBoxConstants.BUTTON_A).toggleWhenPressed(new ShooterCommand(shooterSubsystem));
    //shooterSubsystem.setDefaultCommand(new ShooterCommand(shooterSubsystem));

    new Trigger(this::reverseDaButton).debounce(0.1).whileActiveOnce(new ShooterCommand(shooterSubsystem));
  }

  boolean reverseDaButton() {
    return !daButton.get();
  }

}
