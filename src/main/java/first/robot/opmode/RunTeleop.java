package first.robot.opmode;

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
    final private Timer rumbleTimer = new Timer();
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
    }


    @Override
    public void periodic() {
        /* Called periodically (set time interval) while the robot is enabled. */

        // Shooter.INSTANCE.run();

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

        /*
        // boolean intakeInButton = gamepad1.left_trigger > 0.2;
        //boolean intakeOutButton = gamepad1.left_bumper;
        boolean intakeInButton = gamepad1.a;
        boolean intakeOutButton = gamepad1.b;
        if (intakeOutButton && intakeInButton) {
            intakeInButton = false;
            intakeOutButton = false;
        }

        boolean kickerBackButton = gamepad1.right_bumper;
        boolean kickerOnButton = gamepad1.right_trigger > 0.3;
        if (kickerOnButton && kickerBackButton) {
            kickerOnButton = false;
        }

        boolean shooterButtonHigh =  gamepad1.dpad_up;
        boolean shooterButtonMedium = gamepad1.dpad_right;
        boolean shooterButtonLow = gamepad1.dpad_down;
        boolean shooterButtonOff = gamepad1.dpad_left;

        // INTAKE CODE
        if (intakeInButton) {
            Intake.INSTANCE.intakein();
            telemetry.addLine("Intake: In");
        } else if (intakeOutButton) {
            Intake.INSTANCE.intakeout();
            telemetry.addLine("Intake: Out");
        } else {
            Intake.INSTANCE.intakeoff();
            telemetry.addLine("Intake: Off");

        }

        if (shooterButtonHigh) {
            Shooter.INSTANCE.high();
            telemetry.addLine("Shooter: ShootHigh");
        }
        if (shooterButtonMedium) {
            Shooter.INSTANCE.medium();
            telemetry.addLine("Shooter: shootMid");
        }
        if (shooterButtonLow) {
            Shooter.INSTANCE.low();
            telemetry.addLine("Shooter: ShootLow");
        }
        if (shooterButtonOff) {
            Shooter.INSTANCE.stop();
            telemetry.addLine("Shooter: Stop");
        }

        if (kickerOnButton) {
            Shooter.INSTANCE.kickeron();
            telemetry.addLine("Kicker: In");
        } else if (kickerBackButton) {
            Shooter.INSTANCE.kickerout();
            telemetry.addLine("Kicker: Out");
        } else {
            Shooter.INSTANCE.kickeroff();
            telemetry.addLine("Kicker: Off");
        }

        */

        if (userControls.getGamepad(1).getButtonPressed(Button.LEFT_BUMPER)) {
            robot.drive.autoAim();
            //telemetry.addData("Drive: ","AutoAim()");
        } else {
            double drive = 1. * squareInput(userControls.getGamepad(1).getLeftY());
            double strafe = 1. * squareInput(userControls.getGamepad(1).getLeftX());
            double turn = 1. * squareInput(userControls.getGamepad(1).getRightX());
            robot.drive.moveRobot(drive, strafe, turn);
            // telemetry.addData("Drive: ","powers: %5.2f / %5.2f / %5.2f",drive,strafe,turn);
        }

        // telemetry.update();
    }

    public double squareInput(double stick) {
        return stick*stick*stick;
    }
}
