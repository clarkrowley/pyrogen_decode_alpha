package first.robot.subsystems;

import org.wpilib.hardware.bus.I2C.Port;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.smartdashboard.SmartDashboard;

import first.robot.sensors.GoBildaPinpoint;

public class PinPoint {
    // Create an instance of the sensor

    GoBildaPinpoint pinpoint;

    public PinPoint() {
        // Get a reference to the sensor
        pinpoint = new GoBildaPinpoint(Port.PORT_0);

        // Configure the sensor
        configurePinpoint();

        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.resetPosAndIMU();
    }

    public Pose2d getPose2d() {
        pinpoint.update();
        return pinpoint.getPose();
    }

    public void m_periodic() {
        SmartDashboard.putNumber("pinpoint X", getPose2d().getX());
        SmartDashboard.putNumber("pinpoint Y", getPose2d().getY());
        SmartDashboard.putNumber("pinpoint heading", getPose2d().getRotation().getDegrees());
    }

    public void configurePinpoint(){
       /*
        *  Set the odometry pod positions relative to the point that you want the position to be measured from.
        *
        *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
        *  Left of the center is a positive number, right of center is a negative number.
        *
        *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
        *  Forward of center is a positive number, backwards is a negative number.
        */
        pinpoint.setOffsetsMM(90., -42.5); //these are tuned for 3110-0002-0001 Product Insight #1

        /*
         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
         * number of ticks per unit of your odometry pod.  For example: 
         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
         */
        pinpoint.setEncoderResolution(19.8943678865);

        /*
         * Set the direction that each of the two odometry pods count. The X (forward) pod should
         * increase when you move the robot forward. And the Y (strafe) pod should increase when
         * you move the robot to the left.
         */
        pinpoint.setEncoderDirections(GoBildaPinpoint.EncoderDirection.FORWARD,
                                      GoBildaPinpoint.EncoderDirection.REVERSED);

        /*
         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
         * The IMU will automatically calibrate when first powered on, but recalibrating before running
         * the robot is a good idea to ensure that the calibration is "good".
         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
         * This is recommended before you run your autonomous, as a bad initial calibration can cause
         * an incorrect starting value for x, y, and heading.
         */
        pinpoint.resetPosAndIMU();
    }
}
