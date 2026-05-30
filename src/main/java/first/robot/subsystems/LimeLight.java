
package first.robot.subsystems;

import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Pose3d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.networktables.NetworkTable;
import org.wpilib.networktables.NetworkTableEntry;
import org.wpilib.networktables.NetworkTableInstance;
import org.wpilib.smartdashboard.SmartDashboard;

import first.robot.utils.LimelightHelpers;
import first.robot.utils.LimelightHelpers.PoseEstimate;

public class LimeLight {

    private final NetworkTable table = NetworkTableInstance.getDefault().getTable(getName());
    private final NetworkTableEntry latency = table.getEntry("tl");
    private final NetworkTableEntry tagId = table.getEntry("tid");
    private final NetworkTableEntry hb = table.getEntry("hb");
    private int currentTagId = 0;

    private boolean I_AM_BLUE;
    private int BLUE_TAG_ID =  20;
    private int RED_TAG_ID =  24;

    public static class TargetPose {
        public Pose3d pose;
        public double range;
        public double bearing;
        public double yaw;
        public int id;
    }
    public TargetPose targetPose;

    public LimeLight()
    {
        setPipeline(0);

        targetPose = new TargetPose();
        targetPose.id = 0;

    }

    public void setAlliance(boolean iAmBlue) {
        I_AM_BLUE = iAmBlue;
    }

    public TargetPose getTargetPose() {
        int targetId = I_AM_BLUE ? BLUE_TAG_ID : RED_TAG_ID;
        TargetPose targetPose = new TargetPose();
        targetPose.id = 0;

        currentTagId = getTagId();

        if (currentTagId == targetId) {
            Pose3d pose = LimelightHelpers.getTargetPose3d_CameraSpace(getName());  
            targetPose.pose = pose;
            targetPose.range = pose.getTranslation().toTranslation2d().getNorm();
            targetPose.bearing = pose.getTranslation().toTranslation2d().getAngle().getDegrees();
            targetPose.yaw = pose.getRotation().toRotation2d().getDegrees();
            targetPose.id = currentTagId;
        }
        return targetPose;
    }

    public double getTargetBearing() {
        double offset = 0.;
        currentTagId = getTagId();
        if (currentTagId > 0) {
            return offset - LimelightHelpers.getTX(getName());
        }
        return 0.;
    }

    public void m_periodic() {
        currentTagId = getTagId();
        if(currentTagId > 0) {
        // lastPose2d = new Pose2d(lastBotPose[0],lastBotPose[1], new Rotation2d(lastBotPose[5]));
        //SmartDashboard.putNumber(getPosition()+"Botpose X: ",lastBotPose[0]);
        //SmartDashboard.putNumber(getPosition()+"Botpose Y: ",lastBotPose[1]);
        //SmartDashboard.putNumber(getPosition()+"Botpose Yaw: ",lastBotPose[5]);
        }       
        SmartDashboard.putNumber("hb: ",(double) hb.getNumber(0));
        SmartDashboard.putNumber(getPosition()+"Latency: ",(double) latency.getNumber(0));
        SmartDashboard.putNumber(getPosition()+"TagID: ",tagId.getInteger(-1));
        SmartDashboard.putBoolean(getPosition()+"HasTarget: ",(currentTagId>0));
        //SmartDashboard.putNumber(getPosition()+"Limelight Xoffset: ",getXOffset());
        //SmartDashboard.putNumber(getPosition()+"Limelight Yoffset: ",getYOffset());
        // This method will be called once per scheduler run
    }

    /**
     * Gets the name of the limelight.
     * @return The name of the limelight as a string
     */
    public String getName() {
        return "limelight";
    }

    /**
     * Gets the position of the limelight.
     * @return The position of the limelight as a string
     */
    public String getPosition() {
        return "left";
    }
    /**
     * Toggles between pipeline 0 and 1.
     */
    public void setPipeline(int pipeline) {
        LimelightHelpers.setPipelineIndex(getName(), pipeline);
    }

  /**
   * Returns the Apriltags estimated pose or null if there is no pose.
   * @return The limelight's outputted pose
   */
  public Pose2d getBotPose2dMT1() {
    return LimelightHelpers.getBotPoseEstimate_wpiBlue(getName()).pose;
  }

  public PoseEstimate getMT1PoseEstimate() {
    return LimelightHelpers.getBotPoseEstimate_wpiBlue(getName());
  }

  public Pose2d getBotPose2dMT2() {
    return LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(getName()).pose;
  }

  public PoseEstimate getMT2PoseEstimate() {
    return LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(getName());
  }

  /**
   * Returns the Apriltags estimated pose in target space or null if there is no pose.
   * @return The limleight's ouputted target space pose
   */
  public Pose2d getBotPose2dTargetSpace() {
    return new Pose2d(getBotPoseTargetSpace()[2], getBotPoseTargetSpace()[0], new Rotation2d(Math.toRadians(getBotPoseTargetSpace()[4])));
  }

  /**
   * Returns the latency of the pipeline 
   * @return
   */
  public double getLatency() {
    return latency.getDouble(0);
  }

  /**
   * Gets the full array of bot pose values in target space
   * @return An array of all pose values
   */
  public double[] getBotPoseTargetSpace() {
    return LimelightHelpers.getBotPose_TargetSpace(getName());
  }
  /**
   * Gets the ID of the biggest tag in frame.
   * @return The ID of the tag
   */
  public int getTagId() {
    return (int) tagId.getInteger(0);
  }
  /**
   * Gets the number of tags that the limelight can see.
   * @return The number of tags
   */
  public int getTagCount() {
    return LimelightHelpers.getTargetCount(getName());
  }

  /**
   * Gets the X Offset of the tag from the center of the frame.
   * @return The X Offset
   */
  public double getXOffset() {
    return LimelightHelpers.getTX(getName());
  }
  /**
   * Gets the Y Offset of the tag from the center of frame.
   * @return The Y Offset
   */
  public double getYOffset() {
    return LimelightHelpers.getTY(getName());
  }
  /**
   * Gets the area of the tag in frame
   * @return The area of the tag
   */
  public double getTargetArea() {
    return LimelightHelpers.getTA(getName());
  }
  /**
   * Turns the limelight LEDs on
   */
  public void setLedsOn() {
    LimelightHelpers.setLEDMode_ForceOn(getName());
  }

  public int getPipeline() {
    return (int) LimelightHelpers.getCurrentPipelineIndex(getName());
  }

}
