package lab4;

import static lab4.Resources.*;

//static import to avoid duplicating variables and make the code easier to read
import static lab4.Resources.*;

public class Navigation implements Runnable{
  private static double x, y; 
  private static double deltaX, deltaY;
  
  private static double minDistance;
  private static double theta1, theta2;
  
  public static int[][] waypoints;
  private static int rightDist, leftDist;
  
  // True if bang bang controller isn't using the motors, else false
  // BangBangController changes this boolean, Navigation just checks its value before moving
  public static boolean navigating; 
  
  /**
   * Class constructor
   */
  public Navigation() {
    waypoints = new int[5][2]; 
    leftDist = 10;
    rightDist = 10;
    
    //waypoints
    waypoints[0] = new int[] {2,2};
    waypoints[1] = new int[] {1,3};
    waypoints[2] = new int[] {3,3};
    waypoints[3] = new int[] {3,2};
    waypoints[4] = new int[] {2,1};
    
    // Reset motors, navigating, and set odometer 
    navigating = true;
    leftMotor.stop();
    rightMotor.stop();
    odometer.setXYT(TILE_SIZE, TILE_SIZE, 0);
  }
  
  public boolean isNavigating() {
    return navigating;
  }
  /**
   * Runs the logic of navigation
   */
  public void run () {
    navigating = true;
    for(int i = 0; i < waypoints.length; i++) {
      int xCoord = waypoints[i][0];
      int yCoord = waypoints[i][1];
      travelTo(xCoord, yCoord);
      LCD.drawString("Px Py: " + xCoord + " ," + yCoord, 0, 5);
    }     
    navigating = false;
  }
  
  /**
   * Moves the robot to each desired waypoint
   * @param xCoord  x coordinate of waypoint[i]
   * @param yCoord  y coordinate of waypoint[i]
   */
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
    
    rightMotor.setSpeed(FORWARD_SPEED);
    leftMotor.setSpeed(FORWARD_SPEED);
    // Move
    minDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    leftMotor.rotate(convertDistance(minDistance, WHEEL_RAD), true);
    rightMotor.rotate(convertDistance(minDistance, WHEEL_RAD), false);

  }
  
  /**
   * Causes the robot to turn (on point) to the absolute heading theta. 
   * This method should turn a MINIMAL angle to its target. 
   * @param theta  robot turning angle before each waypoint
   */
  public static void turnTo(double theta) {
    if (theta > 180) {
      theta = 360 - theta;
    }
    else if (theta < -180) {
      theta = 360 + theta;
    }
    
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    leftMotor.rotate(convertAngle(theta, WHEEL_RAD), true);
    rightMotor.rotate(-convertAngle(theta, WHEEL_RAD), false);
   
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