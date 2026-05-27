package first.robot.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    public static final Shooter INSTANCE = new Shooter();
    private Shooter() { }

    private DcMotorEx left_motor = null;
    private DcMotorEx right_motor = null;
    private DcMotorEx kicker = null;

    private String left_name = "left_shooter";
    private String right_name = "right_shooter";
    private String kicker_name = "kicker";


    public static int TICKS_PER_REV = 28;
    public static double VEL_SCALE = 1.;
    public static double PID_P = 20.0;
    public static double PID_I = 0.25;
    public static double PID_D = 0.0;
    public static double PID_F = 0.0;
    static double TARGET_RPM_HIGH = 2740.;
    static double TARGET_RPM_AUTOSHOT = 2250;
    static double TARGET_RPM_MED = 2250.;
    static double TARGET_RPM_LOW = 2000.;
    static double TARGET_VEL_HIGH = TARGET_RPM_HIGH * TICKS_PER_REV * VEL_SCALE / 60;
    static double TARGET_VEL_AUTOSHOT = TARGET_RPM_AUTOSHOT * TICKS_PER_REV * VEL_SCALE / 60;
    static double TARGET_VEL_MED = TARGET_RPM_MED * TICKS_PER_REV * VEL_SCALE / 60;
    static double TARGET_VEL_LOW = TARGET_RPM_LOW * TICKS_PER_REV * VEL_SCALE / 60;



    public void medium() {
        left_motor.setVelocity(TARGET_VEL_MED);
    }

    public void low() {
      left_motor.setVelocity(TARGET_VEL_LOW);
    }

    public void high() {
        left_motor.setVelocity(TARGET_VEL_HIGH);
    }
    public void autoshot(){left_motor.setVelocity(TARGET_VEL_AUTOSHOT);}

    public void stop() {
        left_motor.setVelocity(0.);
        right_motor.setVelocity(0.);
    }

    public void run() {
        double pow = left_motor.getPower();
        right_motor.setPower(pow);
    }

    public void kickeron() {
        kicker.setPower(1.);
    }
    public void kickerslow(){kicker.setPower(0.25);}
    public void kickerout() {
        kicker.setPower(-1.0);
    }
    public void kickeroff() {
        kicker.setPower(0.);
    }

    public void init(HardwareMap hMap) {
        left_motor = hMap.get(DcMotorEx.class,left_name);
        right_motor = hMap.get(DcMotorEx.class,right_name);
        left_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        right_motor.setDirection(DcMotorSimple.Direction.REVERSE);
        left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        kicker = hMap.get(DcMotorEx.class,kicker_name);
        kicker.setDirection(DcMotorSimple.Direction.FORWARD);
        kicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        kicker.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        stop();
        kickeroff();
        PIDFCoefficients pidfNew = new PIDFCoefficients(PID_P,PID_I,PID_D,PID_F);
        left_motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,pidfNew);
    }

}
