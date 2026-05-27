package first.robot.opmode.auto;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.AutoDrive;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class AtWall {

    double driveSpeed = 0.25;
    double slowSpeed = 0.15;
    double minDriveSpeed = 0.05;
    double turnSpeed = 0.20;
    double holdTime = 0.5;

    public static void runTest() {
        ElapsedTime m_timer = new ElapsedTime();



        AutoDrive.INSTANCE.driveStraight(0.2, 0.6, 1., 1.);
        if (AutoSettings.INSTANCE.iAmBlue()) {
            AutoDrive.INSTANCE.turnAndHoldHeading(1., 27., 0.5);
        } else{
            AutoDrive.INSTANCE.turnAndHoldHeading(1., -27., 0.5);
        }
        Shooter.INSTANCE.high();

        m_timer.reset();
        while (m_timer.milliseconds() < 1750.) {Drive.INSTANCE.autoAim(); Shooter.INSTANCE.run(); }

        Shooter.INSTANCE.kickerout();
        m_timer.reset();
        while (m_timer.milliseconds() < 250.) {Drive.INSTANCE.autoAim(); Shooter.INSTANCE.run(); }


        Intake.INSTANCE.intakein();
        Shooter.INSTANCE.kickeron();
        m_timer.reset();
        while (m_timer.milliseconds() < 3000.) {Shooter.INSTANCE.run(); }
        Shooter.INSTANCE.stop();
        Shooter.INSTANCE.kickeroff();

        AutoDrive.INSTANCE.turnAndHoldHeading(1., 0., 0.5);
        m_timer.reset();
        while (m_timer.milliseconds() < 3000.) { }
        if (AutoSettings.INSTANCE.iAmBlue()) {
            AutoDrive.INSTANCE.turnAndHoldHeading(1.,-90,0.5);
        } else {
            AutoDrive.INSTANCE.turnAndHoldHeading(1., 90., 0.5);
        }
        AutoDrive.INSTANCE.driveStraight(0.2, 0.4, -55, 3.);

        m_timer.reset();
        while (m_timer.milliseconds() < 1000.) { }
        Shooter.INSTANCE.kickerslow();
        AutoDrive.INSTANCE.driveStraight(0.2,0.6,30,4.);
        if (AutoSettings.INSTANCE.iAmBlue()) {
        AutoDrive.INSTANCE.turnAndHoldHeading(1., 27., 1.);
        } else {
            AutoDrive.INSTANCE.turnAndHoldHeading(1., -27., 1.);
        }
        Shooter.INSTANCE.high();

        m_timer.reset();
        while (m_timer.milliseconds() < 1000.) {Drive.INSTANCE.autoAim(); Shooter.INSTANCE.run(); }
        Shooter.INSTANCE.kickeroff();
        Intake.INSTANCE.intakeout();
        m_timer.reset();
        while  (m_timer.milliseconds() < 3000.) {Shooter.INSTANCE.run();}
        Shooter.INSTANCE.stop();
        AutoDrive.INSTANCE.driveStraight(0.2,0.6,10,2);
        AutoDrive.INSTANCE.strafeStraight(0.2,0.6,24,3);



       /* m_timer.reset();
        while (m_timer.milliseconds() < 100.) { }
        Shooter.INSTANCE.high();
        Shooter.INSTANCE.kickeron();

        */


        //AutoDrive.INSTANCE.driveStraight(0.2, 0.6, 18., 4.);

    }

}
