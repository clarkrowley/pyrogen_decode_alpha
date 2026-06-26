package first.robot.subsystems;

import org.wpilib.epilogue.Logged;
import org.wpilib.hardware.hal.CANBusMap;

import com.revrobotics.spark.A301;

/*
 * Positive X is forward
 * Positive Y is strafe left
 * Positive Yaw is counter-clockwise
   double frontLeftPower    =  x - y - yaw;
   double frontRightPower   =  x + y + yaw;
   double backLeftPower     =  x + y - yaw;
   double backRightPower    =  x - y + yaw;
*/

@Logged
public class DriveA301 {

    public final A301 left_front = new A301(CANBusMap.CAN_D0);
    public final A301 left_back = new A301(CANBusMap.CAN_D1);
    public final A301 right_front = new A301(CANBusMap.CAN_D13);
    public final A301 right_back = new A301(CANBusMap.CAN_D14);

    public double fwdSpeed;

    public DriveA301() {

        left_front.setInverted(false);
        left_back.setInverted(false);
        right_front.setInverted(true);
        right_back.setInverted(true);

        left_front.setThrottle(0.);
        left_back.setThrottle(0.);
        right_front.setThrottle(0.);
        right_back.setThrottle(0.);

        left_front.setRelativeEncoderPosition(0.);
        left_back.setRelativeEncoderPosition(0.);
        right_front.setRelativeEncoderPosition(0.);
        right_back.setRelativeEncoderPosition(0.);
    }

    public void stop() {
        left_front.setThrottle(0.);
        left_back.setThrottle(0.);
        right_front.setThrottle(0.);
        right_back.setThrottle(0.);
        fwdSpeed = 0.;
    }

    public void moveRobot(double fwd, double strafe, double rot) {
        fwdSpeed = fwd;
        double denominator = Math.max(Math.abs(strafe) + Math.abs(fwd) + Math.abs(rot), 1.0);
        double frontLeftPower = (fwd - strafe - rot) / denominator;
        double frontRightPower = (fwd + strafe + rot) / denominator;
        double backLeftPower = (fwd + strafe - rot) / denominator;
        double backRightPower = (fwd - strafe + rot) / denominator;
        left_front.setThrottle(frontLeftPower);
        right_front.setThrottle(frontRightPower);
        left_back.setThrottle(backLeftPower);
        right_back.setThrottle(backRightPower);
    }

}
