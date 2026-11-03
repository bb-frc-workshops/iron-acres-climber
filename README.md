# Iron Acres Climber

This is the code for our XRP robot. The robot can drive, move an arm, and now **climb**. This guide explains how the climber code works, which files to look at, and how to change it.

You don't need to understand every line. Start by learning where things live and which numbers are safe to change.

---

## The big idea: subsystems, commands, and buttons

Our robot code uses a pattern called **command-based programming**. It has three main parts:

| Part | What it is | Real-world comparison |
|------|------------|-----------------------|
| **Subsystem** | One physical part of the robot, like the drivetrain, the arm, or the climber. It knows how to control its own motors and sensors. | A kitchen appliance, like a blender |
| **Command** | One action a subsystem can do, like "climb up" or "drive forward 10 inches". | Pressing "blend" on the blender |
| **Button binding** | A rule that connects a controller button to a command. | Plugging the blender into an outlet |

To add the climber, we made one of each:

1. a **Climber subsystem**, which controls the motor
2. two **commands**, *climb up* and *climb down*
3. two **button bindings**, Y and X on the controller

---

## The hardware

The XRP board has four motor ports. Two of them are already used by the wheels:

| Port on the board | Channel number in code | Used for |
|-------------------|------------------------|----------|
| Motor L | 0 | Left wheel |
| Motor R | 1 | Right wheel |
| **Motor 3** | **2** | **Climber** ← new! |
| Motor 4 | 3 | (free) |

> **Why is "Motor 3" channel 2?** Programmers usually start counting at **0**, not 1. The first motor is 0, the second is 1, and the third is 2.

Each XRP motor also has an **encoder**, a sensor that counts how far the motor has turned. We use it to see how far the climber has moved.

---

## The files we changed

All the code is in `src/main/java/frc/robot/`.

### 1. `subsystems/Climber.java` (new file)

This file is the climber subsystem. Here are its key pieces:

```java
private final XRPMotor m_motor = new XRPMotor(ClimberConstants.kMotorChannel);
```
This line creates the motor. `XRPMotor` is a tool from WPILib, the robotics library we use. You tell it which channel the motor is plugged into.

```java
public void setSpeed(double speed) {
  m_motor.set(speed);
}
```
This is a **method**: a named set of instructions you can run. Speed is a number from **-1.0 to 1.0**:
- `1.0` = full power one way (up)
- `0.0` = stopped
- `-1.0` = full power the other way (down)

```java
public Command climbUpCommand() {
  return startEnd(() -> setSpeed(ClimberConstants.kClimbUpSpeed), this::stop)
      .withName("ClimbUp");
}
```
This method makes the "climb up" command. `startEnd` takes two jobs:
- **start:** set the motor to the climb-up speed
- **end:** stop the motor

So the motor runs while the command is active and **always stops** when the command ends. That makes it safe.

```java
@Override
public void periodic() {
  SmartDashboard.putNumber("Climber Position (revs)", getPositionRevs());
}
```
`periodic()` runs automatically about **50 times per second**. Here it sends the climber's position to the dashboard so you can watch it change.

### 2. `Constants.java` (edited)

We added a section called `ClimberConstants`. **Constants** are settings that don't change while the robot runs. We keep them in one place so they're easy to find and change.

```java
public static final int kMotorChannel = 2;
public static final boolean kMotorInverted = false;
public static final double kClimbUpSpeed = 1.0;
public static final double kClimbDownSpeed = -0.5;
```

> The `k` at the start of each name is a naming habit that means "this is a constant".

### 3. `RobotContainer.java` (edited)

This file wires everything together. We added three things:

```java
private final Climber m_climber = new Climber();
```
This creates the climber when the robot starts up.

```java
new JoystickButton(m_controller, 4).whileTrue(m_climber.climbUpCommand());
new JoystickButton(m_controller, 3).whileTrue(m_climber.climbDownCommand());
```
These are the button bindings. `whileTrue` means "run this command **while the button is held**". When you let go, the command ends and the motor stops.

---

## How to drive the climber

| Controller button | What happens |
|-------------------|--------------|
| Hold **Y** | Climber goes up |
| Hold **X** | Climber goes down (at half speed) |
| Let go | Climber stops |

---

## Things you can safely change

You can change all of these in `Constants.java` → `ClimberConstants`:

| If this happens… | Change this |
|------------------|-------------|
| Pressing Y makes the climber go **down** | Set `kMotorInverted` to `true` |
| The climber is too fast or too slow going up | Change `kClimbUpSpeed` (keep it between `0.0` and `1.0`) |
| The climber is too fast or too slow going down | Change `kClimbDownSpeed` (keep it between `-1.0` and `0.0`) |
| You plugged the motor into **Motor 4** instead | Set `kMotorChannel = 3`, `kEncoderChannelA = 10`, `kEncoderChannelB = 11` |

**Change one thing at a time**, then test. That way, if something breaks, you know what caused it.

---

## ⚠️ Safety note

The climber has **no automatic limits** yet. If you keep holding a button after the climber reaches the top or bottom, the motor keeps pushing. That can strain the motor or break the mechanism. Let go when it reaches the end!

**Challenge:** Use `getPositionRevs()` to stop the climber automatically when it goes too far. Hint: watch the dashboard number to learn where "top" and "bottom" are.

---

## Building and deploying

1. Open this folder in **WPILib VS Code**.
2. Connect your computer to the XRP's Wi-Fi.
3. Press `Ctrl+Shift+P`, type **WPILib: Simulate Robot Code**, and press Enter. (The XRP runs the code from your computer over Wi-Fi.)
4. In the window that opens, pick **XRP**, switch the robot to **Teleoperated**, and enable it.

> **Building from a terminal instead?** If `gradlew` shows a strange error like `Type T not present`, it's using the wrong version of Java. Run this first in PowerShell:
> ```powershell
> $env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk"
> ```

---

## Words to know

- **Method:** a named set of instructions, like `setSpeed(...)` or `stop()`.
- **Class:** a blueprint for an object. `Climber` is a class.
- **Subsystem:** a class that controls one physical part of the robot.
- **Command:** an action a subsystem performs.
- **Constant:** a setting that never changes while the robot runs.
- **Encoder:** a sensor that counts how far a motor has turned.
- **WPILib:** the free robotics library that provides `XRPMotor`, `Encoder`, commands, and more.
