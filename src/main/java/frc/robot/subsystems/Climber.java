// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.xrp.XRPMotor;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

/** A climbing mechanism driven by a single XRPMotor. */
public class Climber extends SubsystemBase {
  private final XRPMotor m_motor = new XRPMotor(ClimberConstants.kMotorChannel);
  private final Encoder m_encoder =
      new Encoder(ClimberConstants.kEncoderChannelA, ClimberConstants.kEncoderChannelB);

  /** Creates a new Climber. */
  public Climber() {
    m_motor.setInverted(ClimberConstants.kMotorInverted);
    // Report position in output-shaft revolutions
    m_encoder.setDistancePerPulse(1.0 / ClimberConstants.kCountsPerRevolution);
    resetEncoder();
  }

  /**
   * Drive the climber motor directly.
   *
   * @param speed Motor output from -1.0 (down) to 1.0 (up)
   */
  public void setSpeed(double speed) {
    m_motor.set(speed);
  }

  /** Stop the climber motor. */
  public void stop() {
    m_motor.set(0.0);
  }

  /** Reset the climber encoder so the current position reads 0. */
  public void resetEncoder() {
    m_encoder.reset();
  }

  /**
   * Climber position.
   *
   * @return Output-shaft revolutions since the last reset
   */
  public double getPositionRevs() {
    return m_encoder.getDistance();
  }

  /** Runs the climber up while scheduled, stopping when the command ends. */
  public Command climbUpCommand() {
    return startEnd(() -> setSpeed(ClimberConstants.kClimbUpSpeed), this::stop)
        .withName("ClimbUp");
  }

  /** Runs the climber down while scheduled, stopping when the command ends. */
  public Command climbDownCommand() {
    return startEnd(() -> setSpeed(ClimberConstants.kClimbDownSpeed), this::stop)
        .withName("ClimbDown");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Climber Position (revs)", getPositionRevs());
  }
}
