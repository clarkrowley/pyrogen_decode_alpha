package first.robot.opmode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.utils.Pose2d;

@TeleOp(name = "TestLimeLight")
public class TestLimeLight extends OpMode {

    @Override
    public void init() {
        telemetry.addData(">", "Initializing hardware.");
        telemetry.update();
        LimeLight.INSTANCE.init(hardwareMap);
        LimeLight.INSTANCE.setAlliance(true);
        telemetry.addData(">", "Initialization complete.");
        telemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        LimeLight.TargetPose targetPose;
        targetPose = LimeLight.INSTANCE.getTargetPose();
        telemetry.addData("target id: ", targetPose.id);
        if (targetPose.id > 0) {
            double  rangeError      = (targetPose.range - 12.);
            double  headingError    = targetPose.bearing;
            double  yawError        = targetPose.yaw;
        telemetry.addData("targetPose: ","x/y/z: %5.2f / %5.2f / %5.2f",
                targetPose.pose.getPosition().x,
                targetPose.pose.getPosition().y,
                targetPose.pose.getPosition().z);
        telemetry.addData("targetPose: ","y/p/r: %5.2f / %5.2f / %5.2f",
                targetPose.pose.getOrientation().getYaw(),
                targetPose.pose.getOrientation().getPitch(),
                targetPose.pose.getOrientation().getRoll());
        telemetry.addData("to target: ","range/heading/yaw: %5.2f / %5.2f / %5.2f",
                targetPose.range,targetPose.bearing,targetPose.yaw);
        }
        telemetry.update();
    }
}
