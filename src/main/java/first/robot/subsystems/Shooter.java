package first.robot.subsystems;

import static org.wpilib.units.Units.Amp;
import static org.wpilib.units.Units.Volt;

import org.wpilib.epilogue.Logged;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.math.controller.PIDController;
import org.wpilib.math.controller.SimpleMotorFeedforward;
import org.wpilib.units.measure.Voltage;
import org.wpilib.units.VoltageUnit;

@Logged
public class Shooter {

    PIDController pidc = new PIDController(0, 0, 0);
    SimpleMotorFeedforward ff = new SimpleMotorFeedforward(0, 0, 0);

    public final ExpansionHubMotor left_motor = new ExpansionHubMotor(1, 2);
    public final ExpansionHubMotor right_motor = new ExpansionHubMotor(1, 1);
    public final ExpansionHubMotor kicker = new ExpansionHubMotor(0, 2);

    public double kP = 0.007;
    public double kI = 0.0;
    public double kD = 0.0;
    public double kS = 1.0;
    public double kV = 0.002;
    public double kA = 0.0;
    public double RPM = 0.0;

    public static int TICKS_PER_REV = 28;
    public static double VEL_SCALE = 1.;
    static double TARGET_RPM_HIGH = 2740.;
    static double TARGET_RPM_AUTOSHOT = 2250;
    static double TARGET_RPM_MED = 2250.;
    static double TARGET_RPM_LOW = 2000.;
    static double TARGET_VEL_HIGH = TARGET_RPM_HIGH * TICKS_PER_REV * VEL_SCALE / 60;
    static double TARGET_VEL_AUTOSHOT = TARGET_RPM_AUTOSHOT * TICKS_PER_REV * VEL_SCALE / 60;
    static double TARGET_VEL_MED = TARGET_RPM_MED * TICKS_PER_REV * VEL_SCALE / 60;
    static double TARGET_VEL_LOW = TARGET_RPM_LOW * TICKS_PER_REV * VEL_SCALE / 60;

    public Shooter() {
        kicker.setReversed(false);
        kicker.setFloatOn0(false);
        kicker.setEnabled(true);

        left_motor.setReversed(false);
        left_motor.setFloatOn0(false);
        left_motor.setEnabled(true);
        left_motor.setDistancePerCount(1.0);

        right_motor.setReversed(true);
        right_motor.setFloatOn0(false);
        right_motor.setEnabled(true);
        right_motor.setDistancePerCount(1.0);
        //right_motor.follow(left_motor);

        left_motor.setThrottle(0.);
        left_motor.resetEncoder();

        stop();
        kickeroff();
    }

    public void inckS() { kS += .001; }
    public void deckS() { kS -= .001; kA = Math.max(kS, 0); }
    public void inckV() { kV += .001; }
    public void deckV() { kV -= .001; kV = Math.max(kV, 0); }
    public void inckA() { kA += .001; }
    public void deckA() { kA -= .001; kA = Math.max(kA, 0); }
    public void inckP() { kP += .001; }
    public void deckP() { kP -= .001; kP = Math.max(kP, 0); }
    public void inckI() { kI += .001; }
    public void deckI() { kI -= .001; kI = Math.max(kI, 0); }
    public void inckD() { kD += .001; }
    public void deckD() { kD -= .001; kD = Math.max(kD, 0); }

    public void incRPM() { RPM += 100; RPM = Math.min(RPM, 5000); }
    public void decRPM() { RPM -= 100; RPM = Math.max(RPM, 0); }

    public void setPID() {
        pidc.setPID(kP, kI, kD);
    //  left_motor.getVelocityConstants().setPID(kP, kI, kD);
    //  right_motor.getVelocityConstants().setPID(kP, kI, kD);
    }
    public void setFF() {
        ff.setKs(kS);
        ff.setKv(kV);
        ff.setKa(kA);
     // left_motor.getVelocityConstants().setFF(kS, kV, kA);
     // right_motor.getVelocityConstants().setFF(kS, kV, kA);
    }

    public double[] getOuts() {
        double[] vals = {0.,0.,0.,0.};
        double TARGET_VEL = RPM * TICKS_PER_REV * VEL_SCALE / 60;
        pidc.setSetpoint(TARGET_VEL);
        vals[0] = pidc.calculate(left_motor.getEncoderVelocity());
        vals[1] = ff.calculate(TARGET_VEL);
        vals[2] = Voltage.ofBaseUnits(vals[0]+vals[1], Volt).magnitude();
        vals[3] = TARGET_VEL;
        return vals;
    }

    public void setRPM() {
        double TARGET_VEL = RPM * TICKS_PER_REV * VEL_SCALE / 60;
        pidc.setSetpoint(TARGET_VEL);
        double feedback = pidc.calculate(left_motor.getEncoderVelocity());
        double feedfw = ff.calculate(TARGET_VEL);
        left_motor.setVoltage(Voltage.ofBaseUnits(feedfw+feedback, Volt));
        right_motor.setVoltage(Voltage.ofBaseUnits(feedfw+feedback, Volt));
      //left_motor.setVelocitySetpoint(TARGET_VEL);
    }

    public void medium() {
        RPM = TARGET_RPM_MED;
    }

    public void low() {
        RPM = TARGET_RPM_LOW;
    }

    public void high() {
        RPM = TARGET_RPM_HIGH;
    }

    public void autoshot(){
        RPM = TARGET_RPM_AUTOSHOT;
    }

    public void stop() {
        RPM = 0;
        setRPM();
    }

    public void kickeron() {
        kicker.setThrottle(1.);
    }
    public void kickerslow(){
        kicker.setThrottle(0.25);
    }
    public void kickerout() {
        kicker.setThrottle(-1.0);
    }
    public void kickeroff() {
        kicker.setThrottle(0.);
    }

}