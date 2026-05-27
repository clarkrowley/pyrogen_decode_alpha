package first.robot.opmode.auto;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.AutoDrive;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class AtGoal {

    double driveSpeed = 0.25;
    double slowSpeed = 0.15;
    double minDriveSpeed = 0.05;
    double turnSpeed = 0.20;
    double holdTime = 0.5;

    public static void runTest() {
        ElapsedTime m_timer = new ElapsedTime();
        m_timer.reset();
        while (m_timer.milliseconds() < 100.) { }

        // back up to shooting position and shoot preloaded artifacts
        AutoDrive.INSTANCE.driveStraight(0.2,0.6,-60,4);
        Shooter.INSTANCE.autoshot();
        m_timer.reset();
        while (m_timer.milliseconds() < 1500.) {Shooter.INSTANCE.run(); Drive.INSTANCE.autoAim(); }
        Intake.INSTANCE.intakein();
        Shooter.INSTANCE.kickeron();
        m_timer.reset();
        while (m_timer.milliseconds() < 3000.) {Shooter.INSTANCE.run(); }
        Shooter.INSTANCE.kickeroff();
        Intake.INSTANCE.intakeoff();
        Shooter.INSTANCE.stop();
        m_timer.reset();

        // turn to row 1 and intake
        if (AutoSettings.INSTANCE.iAmBlue()) {
            AutoDrive.INSTANCE.turnAndHoldHeading(0.8, -135., 0.5);
        } else {
            AutoDrive.INSTANCE.turnAndHoldHeading(0.8, 135., 0.5);
        }

        Shooter.INSTANCE.kickerslow();
        Intake.INSTANCE.intakein();
        AutoDrive.INSTANCE.driveStraight(0.2,0.6,-38,2.5);

        // drive out to shooting position and turn to the goal
        AutoDrive.INSTANCE.driveStraight(0.2,0.6,35,2.5);
        Shooter.INSTANCE.kickeroff();
        AutoDrive.INSTANCE.turnAndHoldHeading(0.8, 0., 0.5);
        Shooter.INSTANCE.kickerout();
        while (m_timer.milliseconds() < 250.) {}
        Shooter.INSTANCE.kickeroff();

        // spin up shooter and shoot row one artifacts
        Shooter.INSTANCE.autoshot();
        m_timer.reset();
        while (m_timer.milliseconds() < 1500.) {Shooter.INSTANCE.run(); Drive.INSTANCE.autoAim(); }
        Shooter.INSTANCE.kickeron();
        Intake.INSTANCE.intakein();;
        m_timer.reset();
        while (m_timer.milliseconds() < 3000.) {Shooter.INSTANCE.run(); }

        // turn off shooter and intake
        Shooter.INSTANCE.kickeroff();
        Intake.INSTANCE.intakeoff();;
        Shooter.INSTANCE.stop();

        // strafe off the line
        if (AutoSettings.INSTANCE.iAmBlue()) {
            AutoDrive.INSTANCE.strafeStraight(0.2,0.8,24., 3.);
        } else {
            AutoDrive.INSTANCE.strafeStraight(0.2,0.8,-24., 3.);
        }

        /*
        m_timer.reset();
        while (m_timer.milliseconds() < 500.) { }
        if (AutoSettings.INSTANCE.iAmBlue()) {
            AutoDrive.INSTANCE.turnAndHoldHeading(1,-135,0.5);
        } else {
            AutoDrive.INSTANCE.turnAndHoldHeading(1., 135., 0.5);
        }
        AutoDrive.INSTANCE.strafeStraight(0.2,0.6,-24,4);
        m_timer.reset();
        while (m_timer.milliseconds() < 500.) { }
        Intake.INSTANCE.intakein();
        AutoDrive.INSTANCE.driveStraight(0.2,0.6,-36,4);
        m_timer.reset();
        while (m_timer.milliseconds() < 500.) { }
        AutoDrive.INSTANCE.driveStraight(0.2,0.6,34,4);
        m_timer.reset();
        */

    }
}