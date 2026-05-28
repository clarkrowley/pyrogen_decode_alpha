package first.robot.opmode.teleop;

import org.wpilib.driverstation.DefaultUserControls;
import org.wpilib.driverstation.Gamepad.Button;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;
import org.wpilib.smartdashboard.SmartDashboard;

import first.robot.Robot;

@Teleop
public class TuneShooter extends PeriodicOpMode {
    private final Robot robot;
    private final DefaultUserControls userControls;

    double kP, kI, kD, kS, kV, kA, RPM;

    @Override
    public void disabledPeriodic() {
        /* Called periodically (on every DS packet) while the robot is disabled. */
    }

    @Override
    public void end() {
        /* Called when the robot is disabled (after previously being enabled). */
    }

    @Override
    public void close() {
        /* Called when the opmode is de-selected / no additional methods will be called. */
    }

    @Override
    public void start() {
        /* Called once when the robot is enabled. */
    }

    /** The Robot instance is passed into the opmode via the constructor. */
    public TuneShooter(Robot robot, DefaultUserControls userControls) {
        this.robot = robot;
        this.userControls = userControls;
    }

    @Override
    public void periodic() {
        /* Called periodically (set time interval) while the robot is enabled. */

        kP = SmartDashboard.getNumber("kP", kP);
        kI = SmartDashboard.getNumber("kI", kI);
        kD = SmartDashboard.getNumber("kD", kD);
        kS = SmartDashboard.getNumber("kS", kS);
        kV = SmartDashboard.getNumber("kV", kV);
        kA = SmartDashboard.getNumber("kA", kA);

        robot.shooter.kP = kP;
        robot.shooter.kI = kI;
        robot.shooter.kD = kD;
        robot.shooter.kS = kS;
        robot.shooter.kV = kV;
        robot.shooter.kA = kA;

        boolean inckS = userControls.getGamepad(1).getButtonPressed(Button.DPAD_UP); // A button
        boolean deckS = userControls.getGamepad(1).getButtonPressed(Button.DPAD_DOWN); // A button
        boolean inckV = userControls.getGamepad(1).getButtonPressed(Button.DPAD_RIGHT); // A button
        boolean deckV = userControls.getGamepad(1).getButtonPressed(Button.DPAD_LEFT); // A button

        boolean inckP = userControls.getGamepad(1).getButtonPressed(Button.NORTH_FACE); // A button
        boolean deckP = userControls.getGamepad(1).getButtonPressed(Button.SOUTH_FACE); // A button
        boolean inckI = userControls.getGamepad(1).getButtonPressed(Button.EAST_FACE); // A button
        boolean deckI = userControls.getGamepad(1).getButtonPressed(Button.WEST_FACE); // A button

        if (inckS) {robot.shooter.inckS();}
        if (deckS) {robot.shooter.deckS();}
        if (inckV) {robot.shooter.inckV();}
        if (deckV) {robot.shooter.deckV();}
        if (inckP) {robot.shooter.inckP();}
        if (deckP) {robot.shooter.deckP();}
        if (inckI) {robot.shooter.inckI();}
        if (deckI) {robot.shooter.deckI();}

        kP = robot.shooter.kP;
        kI = robot.shooter.kI;
        kD = robot.shooter.kD;
        kS = robot.shooter.kS;
        kV = robot.shooter.kV;
        kA = robot.shooter.kA;

        SmartDashboard.putNumber("kP", kP);
        SmartDashboard.putNumber("kI", kI);
        SmartDashboard.putNumber("kD", kD);
        SmartDashboard.putNumber("kS", kS);
        SmartDashboard.putNumber("kV", kV);
        SmartDashboard.putNumber("kA", kA);

        SmartDashboard.getNumber("RPM", RPM);
        robot.shooter.RPM = RPM;
        boolean incRPM = userControls.getGamepad(1).getRightTriggerAxis() > 0.3;
        boolean decRPM = userControls.getGamepad(1).getLeftTriggerAxis() > 0.3;
        if (incRPM) {robot.shooter.incRPM();}
        if (decRPM) {robot.shooter.decRPM();}
        RPM = robot.shooter.RPM;
        SmartDashboard.putNumber("RPM", RPM);

        robot.shooter.setFF();
        robot.shooter.setPID();
        robot.shooter.setRPM();

        double m_vel_left = robot.shooter.left_motor.getEncoderVelocity();
        double m_vel_right = robot.shooter.right_motor.getEncoderVelocity();
        SmartDashboard.putNumber("left motor speed",m_vel_left);
        SmartDashboard.putNumber("right motor speed",m_vel_right);

        double drive = -1. * squareInput(userControls.getGamepad(1).getLeftY());
        double strafe = -1. * squareInput(userControls.getGamepad(1).getLeftX());
        double turn = -1. * squareInput(userControls.getGamepad(1).getRightX());
        robot.drive.moveRobot(drive, strafe, turn);
    }

    public double squareInput(double stick) {
        return stick*stick*stick;
    }
}
