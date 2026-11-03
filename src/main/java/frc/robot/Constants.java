// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class ClimberConstants {
    // XRP motor channel 3 is the "Motor 4" port on the board. Its built-in
    // encoder is wired to DIO 10/11.
    public static final int kMotorChannel = 3;
    public static final int kEncoderChannelA = 10;
    public static final int kEncoderChannelB = 11;

    // Flip this if positive output drives the climber the wrong way
    public static final boolean kMotorInverted = true;

    // Standard XRP motor: 12 counts per motor rev * 48.75:1 gearbox
    public static final double kCountsPerRevolution = 585.0;

    public static final double kClimbUpSpeed = 1.0;
    public static final double kClimbDownSpeed = -0.5;
  }
}
