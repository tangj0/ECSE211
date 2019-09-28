package lab3;

import lab3.PController;

//static import to avoid duplicating variables and make the code easier to read
import static lab3.Resources.*;

public class Navigation {
  
  private static double currentTheta;
  
  private static double rotationTheta;
  public boolean isNavigating() {
    return true;
  }
  
  public void travelTo(double x, double y) {
    
  }
  
  /*
   * This method causes the robot to turn (on point) to the absolute heading theta. 
   * This method should turn a MINIMAL angle to its target. 
   */
  public void turnTo(double theta) {
    currentTheta = Odometer.getOdometer().getXYT()[2];
    rotationTheta = theta - currentTheta;
    
    if (rotationTheta > 180) {
      rotationTheta -= 360;
    }
    
    if (rotationTheta < 0) {
      leftMotor.setSpeed(ROTATE_SPEED);
      rightMotor.setSpeed(ROTATE_SPEED);

      leftMotor.rotate(convertAngle(rotationTheta, WHEEL_RAD), true);
      rightMotor.rotate(-convertAngle(rotationTheta), WHEEL_RAD), false);
    }
    else {
      
    }
    convertAngle(rotationTheta, WHEEL_RAD);
  }
  
  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance, double wheelRad) {
    return (int) ((180.0 * distance) / (Math.PI * wheelRad));
  }
  
  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that
   * angle.
   * 
   * @param angle
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle, double wheelRad) {
    return convertDistance(Math.PI * TRACK * angle / 360.0, wheelRad);
  }
  
}