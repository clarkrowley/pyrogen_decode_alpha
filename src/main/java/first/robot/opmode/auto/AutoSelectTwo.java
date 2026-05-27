package first.robot.opmode.auto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.subsystems.ActiveOpMode;
import org.firstinspires.ftc.teamcode.subsystems.AutoDrive;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Odometry;
import org.firstinspires.ftc.teamcode.subsystems.PinPoint;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.ArrayList;

@Autonomous(name="AutoSelectTwo", preselectTeleOp = "runTeleop")

public class AutoSelectTwo extends LinearOpMode
{

    final private ElapsedTime timer = new ElapsedTime();

    ArrayList<Boolean> buttonArray = new ArrayList<>();
    int booleanIncrementer = 0;

    @Override
    public void runOpMode() {

        telemetry.addData(">","initializing hardware");
        telemetry.update();
        ActiveOpMode.INSTANCE.init(this);
        AutoSettings.INSTANCE.readAutoConfig();
        Drive.INSTANCE.init(hardwareMap);
        LimeLight.INSTANCE.init(hardwareMap);
        LimeLight.INSTANCE.setAlliance(AutoSettings.INSTANCE.iAmBlue());
        Intake.INSTANCE.init(hardwareMap);
        PinPoint.INSTANCE.init(hardwareMap);
        Shooter.INSTANCE.init(hardwareMap);
        Intake.INSTANCE.init(hardwareMap);
        Odometry.INSTANCE.teleinit();
        AutoDrive.INSTANCE.init(telemetry, hardwareMap);
        telemetry.update();
        telemetry.addData(">","hardware init complete.");

        AutoSettings.INSTANCE.readAutoConfig();
        telemetry.addData(">", "autosettings init complete.");
        telemetry.update();

        telemetry.addData(">","initialization complete.");
        telemetry.update();

        while (!isStarted()) {

            boolean g1xPressed = ifPressed(gamepad1.x);
            boolean g1dpdPressed = ifPressed(gamepad1.dpad_down);
            boolean g1dpuPressed = ifPressed(gamepad1.dpad_up);
            boolean g1dplPressed = ifPressed(gamepad1.dpad_left);
            boolean g1dprPressed = ifPressed(gamepad1.dpad_right);
            boolean g1backPressed = ifPressed(gamepad1.back);

            if (g1dpuPressed) {
                AutoSettings.INSTANCE.I_AM_BLUE = !AutoSettings.INSTANCE.I_AM_BLUE;
            }
            if (g1dplPressed) {
                AutoSettings.INSTANCE.PEDRO_GOAL = !AutoSettings.INSTANCE.PEDRO_GOAL;
            }
            if (g1dprPressed) {
                AutoSettings.INSTANCE.ROW_COUNT = (AutoSettings.INSTANCE.ROW_COUNT + 1) % 4;
            }

                String myAlliance = AutoSettings.INSTANCE.iAmBlue() ? "Blue" : "Red";
                String myPosition = AutoSettings.INSTANCE.pedroGoal() ? "AtGoal" : "AtWall";
                int myRowCount = AutoSettings.INSTANCE.rowcount();

                if (booleanIncrementer != 0) {
                    AutoSettings.INSTANCE.saveAutoConfig();
                    LimeLight.INSTANCE.setAlliance(AutoSettings.INSTANCE.iAmBlue());
                }
                booleanIncrementer = 0;

                telemetry.addData("Alliance Color (Dpad Up):", myAlliance);
                telemetry.addData("Start Position (Dpad Left):", myPosition);
                telemetry.addData("Rows to intake (Dpad Right):", myRowCount);
                telemetry.addData("Obelisk Tag ID (Dpad Right);", LimeLight.INSTANCE.getObeliskTag());
                telemetry.addLine(" ");

                telemetry.addData(">", "Touch Play to start OpMode");
                telemetry.update();
                sleep(20);
            }

            timer.reset();

            if (AutoSettings.INSTANCE.pedroGoal()) {
                AtGoal.runTest();
            } else {
                AtWall.runTest();
            }


            telemetry.addData("Robot Pos x/y", "%7f/%7f", Odometry.INSTANCE.getX(),
                    Odometry.INSTANCE.getY());
            telemetry.addData("Robot heading deg", "%7f", Odometry.INSTANCE.getHeading());
            telemetry.update();
        }

        private  boolean ifPressed(boolean button) {
            boolean output = false;
            boolean buttonWas;
            if (buttonArray.size() == booleanIncrementer) {
                buttonArray.add(false);
            }
            buttonWas = buttonArray.get(booleanIncrementer);
            if (button != buttonWas && buttonWas) {
                output = true;
            }
            buttonArray.set(booleanIncrementer, button);
            booleanIncrementer += 1;
            return output;

        }
}



