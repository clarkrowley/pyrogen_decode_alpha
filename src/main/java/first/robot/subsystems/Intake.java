package first.robot.subsystems;

import org.wpilib.hardware.expansionhub.ExpansionHubMotor;

public class Intake {
    private final ExpansionHubMotor intake = new ExpansionHubMotor(0, 1);
    private Drive drive;

    public Intake(Drive drive) {
        this.drive = drive;
        intake.setReversed(true);
        intake.setFloatOn0(false);
        intake.setEnabled(true);
        intake.setThrottle(0.);
        intake.resetEncoder();
    }

    private double INTAKE_OUT_POWER = 0.59;
    private double INTAKE_IN_POWER = -1.0;
    private double INTAKE_MIN_POWER = -0.4;

    public void intakein(){
        intake.setThrottle(INTAKE_IN_POWER);
    }
    public void intakeout(){
        intake.setThrottle(INTAKE_OUT_POWER);
    }
    public void intakeoff() {
        double minSpeed = 0.;
        if (drive.fwdSpeed > 0.1) {
            minSpeed = INTAKE_MIN_POWER;
        }
        intake.setThrottle(minSpeed);
    }

}