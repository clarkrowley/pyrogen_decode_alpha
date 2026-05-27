package first.robot.subsystems;

import org.wpilib.epilogue.Logged;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;


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
public class Drive {

    private LimeLight limeLight;

    public final ExpansionHubMotor left_front = new ExpansionHubMotor(0, 0);
    public final ExpansionHubMotor right_front = new ExpansionHubMotor(0, 1);
    public final ExpansionHubMotor left_back = new ExpansionHubMotor(0, 2);
    public final ExpansionHubMotor right_back = new ExpansionHubMotor(0, 3);

    public double fwdSpeed;

    public Drive(LimeLight limeLight) {

        this.limeLight = limeLight;

        left_front.setReversed(false);
        left_back.setReversed(false);
        right_front.setReversed(true);
        right_back.setReversed(true);

        left_front.setFloatOn0(false);
        left_back.setFloatOn0(false);
        right_front.setFloatOn0(false);
        right_back.setFloatOn0(false);

        left_front.setEnabled(true);
        left_back.setEnabled(true);
        right_front.setEnabled(true);
        right_back.setEnabled(true);

        left_front.setThrottle(0.);
        left_back.setThrottle(0.);
        right_front.setThrottle(0.);
        right_back.setThrottle(0.);

        left_front.resetEncoder();
        left_back.resetEncoder();
        right_front.resetEncoder();
        right_back.resetEncoder();
    }

    public void stop() {
        left_front.setThrottle(0.);
        left_back.setThrottle(0.);
        right_front.setThrottle(0.);
        right_back.setThrottle(0.);
    }

    public void autoAim() {
        final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)
        final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)
        double headingError = limeLight.getTargetBearing();
        double turn   = Math.clamp(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
        moveRobot(0.,0.,turn);
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
