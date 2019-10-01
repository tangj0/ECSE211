package lab3;

import static lab3.Resources.*;

//static import to avoid duplicating variables and make the code easier to read
import static lab3.Resources.*;

public class Navigation {
  
  private static double currentTheta;
  private static double rotationTheta;
  
  private static double x, y; 
  private static double deltaX, deltaY;
  
  private static double minDistance;
  private static double theta1, theta2;
  
  public static int[][] waypoints;
  
  // True if bang bang controller isn't using the motors, else false
  // BangBangController changes this boolean, Navigation just checks its value before moving
  public static boolean navigating; 
  
//  public Navigation() {
//    waypoints = new int[5][2]; 
//    
//    //waypoints from map 1
//    waypoints[0] = new int[] {1,3};
//    waypoints[1] = new int[] {2,2};
//    waypoints[2] = new int[] {3,3};
//    waypoints[3] = new int[] {3,2};
//    waypoints[4] = new int[] {2,1};
//
//  }
  
//  public Navigation() {
//    // Reset motors, navigating, and set odometer 
//    leftMotor.stop();
//    rightMotor.stop();
//    odometer.setXYT(TILE_SIZE, TILE_SIZE, 0);
//    navigating = true;
//  }
  
//  public void run() {
//    // Reset motors, navigating, and set odometer 
//    leftMotor.stop();
//    rightMotor.stop();
//    odometer.setXYT(TILE_SIZE, TILE_SIZE, 0);
//    navigating = true;
  
//    for(int i = 0; i < waypoints.length; i++) {
//      int xCoord = waypoints[i][0];
//      int yCoord = waypoints[i][1];
//      travelTo(xCoord, yCoord);
//    }
      
//  }
  
  public static void travelTo(double xCoord, double yCoord) {
    // Gets current x, y positions (already in cm) 
    x = odometer.getXYT()[0];
    y = odometer.getXYT()[1];
    
    deltaX = TILE_SIZE*xCoord - x; 
    deltaY = TILE_SIZE*yCoord - y;
    
    // Turn
    theta2 = Math.toDegrees(Math.atan2(deltaX, deltaY)); //theta2 now in degrees
    theta1 = odometer.getXYT()[2]; // theta1 in degrees
    
    turnTo(theta2 - theta1);
    
    rightMotor.stop();
    leftMotor.stop();
    
    // Move
    if (navigating) {
      minDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
      leftMotor.rotate(convertDistance(minDistance, WHEEL_RAD), true);
      rightMotor.rotate(convertDistance(minDistance, WHEEL_RAD), false);
    }
  }
  
  /*
   * This method causes the robot to turn (on point) to the absolute heading theta. 
   * This method should turn a MINIMAL angle to its target. 
   */
  public static void turnTo(double theta) {
    // Convert theta to minimum angle
    if (theta > 180) {
      theta = 360 - theta;
    }
    else if (theta < -180) {
      theta = 360 + theta;
    }
    
    LCD.drawString("turn Angle: " + theta, 0, 3);
    
    if (navigating) {
      leftMotor.setSpeed(ROTATE_SPEED);
      rightMotor.setSpeed(ROTATE_SPEED);
      leftMotor.rotate(convertAngle(theta, WHEEL_RAD), true);
      rightMotor.rotate(-convertAngle(theta, WHEEL_RAD), false);
    }
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
  
  /**
   * Sleeps current thread for the specified duration.
   * 
   * @param duration sleep duration in milliseconds
   */
  public static void sleepFor(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      // There is nothing to be done here
    }
  }
  
  
}