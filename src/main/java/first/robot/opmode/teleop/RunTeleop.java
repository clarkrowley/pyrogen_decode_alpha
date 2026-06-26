package first.robot.opmode.teleop;

import org.wpilib.driverstation.DefaultUserControls;
import org.wpilib.driverstation.Gamepad.Button;
import org.wpilib.driverstation.GenericHID.RumbleType;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;
import org.wpilib.system.Timer;

import first.robot.Robot;

/*
import first.robot.opmode.auto.AutoSettings;
import first.robot.subsystems.LimeLight;
import first.robot.subsystems.Shooter;
import first.robot.subsystems.Intake;
import first.robot.subsystems.Odometry;
import first.robot.subsystems.PinPoint;
*/

@Teleop
public class RunTeleop extends PeriodicOpMode {
    private final Robot robot;
    private final DefaultUserControls userControls;

    final private Timer teleopTimer = new Timer();
    final private Timer blinkTimer = new Timer();
    boolean endGameWarning = false;

    @Override
    public void disabledPeriodic() {
        /* Called periodically (on every DS packet) while the robot is disabled. */
    }

    @Override
    public void end() {
        /* Called when the robot is disabled (after previously being enabled). */
    }

    @Override
    public void close() {
        /* Called when the opmode is de-selected / no additional methods will be called. */
    }

    /** The Robot instance is passed into the opmode via the constructor. */
    public RunTeleop(Robot robot, DefaultUserControls userControls) {
        this.robot = robot;
        this.userControls = userControls;

        /*
        telemetry.addData(">", "Initializing hardware.");
        telemetry.update();
        AutoSettings.INSTANCE.readAutoConfig();
        Drive.INSTANCE.init(robot);
        LimeLight.INSTANCE.init(robot);
        LimeLight.INSTANCE.setAlliance(AutoSettings.INSTANCE.iAmBlue());
        Shooter.INSTANCE.init(robot);
        Intake.INSTANCE.init(robot);
        PinPoint.INSTANCE.init(robot);
        Odometry.INSTANCE.teleinit();
        telemetry.addData(">", "Initialization complete.");
        telemetry.update();
        */
    }

    @Override
    public void start() {
        /* Called once when the robot is enabled. */
        teleopTimer.reset();
        endGameWarning = false;
        robot.shooter.stop();
    }


    @Override
    public void periodic() {
        /* Called periodically (set time interval) while the robot is enabled. */

        robot.shooter.setRPM();

        if (teleopTimer.get() >= 80. & teleopTimer.get() <= 90.) {
            if (! endGameWarning) {
                blinkTimer.reset();
                endGameWarning = ! endGameWarning;
            }
            double m_time = blinkTimer.get();
            double m_round = Math.round(m_time);
            //if (Math.round(m_time) % 2 == 1 && m_time-Math.round(m_time) < 0.25) {
            if (m_round % 2 == 1 && m_time-m_round < 0.25) {
                userControls.getGamepad(1).setRumble(RumbleType.LEFT_RUMBLE,1.0);
            }
            else {
                userControls.getGamepad(1).setRumble(RumbleType.LEFT_RUMBLE,0.0);
            }
        }

        //boolean intakeInButton = userControls.getGamepad(1).getLeftTriggerAxis().greaterThan(0.3);
        boolean intakeInButton = userControls.getGamepad(1).getButtonPressed(Button.SOUTH_FACE); // A button
        boolean intakeOutButton = userControls.getGamepad(1).getButtonPressed(Button.EAST_FACE); // B button

        if (intakeOutButton && intakeInButton) {
            intakeInButton = false;
            intakeOutButton = false;
        }

        boolean kickerOnButton = userControls.getGamepad(1).getRightTriggerAxis() > 0.3;
        boolean kickerBackButton = userControls.getGamepad(1).getButtonPressed(Button.RIGHT_BUMPER);
        if (kickerOnButton && kickerBackButton) {
            kickerOnButton = false;
        }

        boolean shooterButtonHigh =  userControls.getGamepad(1).getButtonPressed(Button.DPAD_UP);
        boolean shooterButtonMedium =  userControls.getGamepad(1).getButtonPressed(Button.DPAD_RIGHT);
        boolean shooterButtonLow =  userControls.getGamepad(1).getButtonPressed(Button.DPAD_DOWN);
        boolean shooterButtonOff =  userControls.getGamepad(1).getButtonPressed(Button.DPAD_LEFT);

        // INTAKE CODE
        if (intakeInButton) {
            robot.intake.intakein();
            //telemetry.addLine("Intake: In");
        } else if (intakeOutButton) {
            robot.intake.intakeout();
            //telemetry.addLine("Intake: Out");
        } else {
            robot.intake.intakeoff();
            //telemetry.addLine("Intake: Off");

        }

        if (shooterButtonHigh) {
            robot.shooter.high();
        }
        if (shooterButtonMedium) {
            robot.shooter.medium();
        }
        if (shooterButtonLow) {
            robot.shooter.low();
        }
        if (shooterButtonOff) {
            robot.shooter.stop();
        }

        if (kickerOnButton) {
            robot.shooter.kickeron();
        } else if (kickerBackButton) {
            robot.shooter.kickerout();
        } else {
            robot.shooter.kickeroff();
        }

        double drive = -1. * squareInput(userControls.getGamepad(1).getLeftY());
        double strafe = -1. * squareInput(userControls.getGamepad(1).getLeftX());
        double turn = -1. * squareInput(userControls.getGamepad(1).getRightX());
        robot.drive.moveRobot(drive, strafe, turn);
        // telemetry.addData("Drive: ","powers: %5.2f / %5.2f / %5.2f",drive,strafe,turn);

        // telemetry.update();
    }

    public double squareInput(double stick) {
        return stick*stick*stick;
    }
}
