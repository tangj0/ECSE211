package lab3;

import static lab3.Resources.*;

//static import to avoid duplicating variables and make the code easier to read
import static lab3.Resources.*;

public class Navigation implements Runnable {
  
  private static double currentTheta;
  private static double rotationTheta;
  
  private static double x, y; 
  private static double deltaX, deltaY;
  
  private static double minDistance;
  private static double theta1, theta2;
  
  public static int[][] waypoints;
  
  public Navigation() {
    waypoints = new int[5][2]; 
    
    //waypoints from map 1
    waypoints[0] = new int[] {1,3};
    waypoints[1] = new int[] {2,2};
    waypoints[2] = new int[] {3,3};
    waypoints[3] = new int[] {3,2};
    waypoints[4] = new int[] {2,1};

  }
  
  public void run() {
    // Reset motors and set odometer 
    leftMotor.stop();
    rightMotor.stop();
    odometer.setXYT(TILE_SIZE, TILE_SIZE, 0);
     
    for(int i = 0; i < waypoints.length; i++) {
      int xCoord = waypoints[i][0];
      int yCoord = waypoints[i][1];
      travelTo(xCoord, yCoord);
    }
      
  }
  
  // True if motors are moving
  public boolean isNavigating() {
    boolean navigating = false;
    if (leftMotor.isMoving() || rightMotor.isMoving()) {
      navigating = true;
    }
    return navigating;
  }
  
  public void travelTo(double xCoord, double yCoord) {
    
    // Gets current x, y positions (already in cm) 
    x = odometer.getXYT()[0];
    y = odometer.getXYT()[1];
    
    deltaX = TILE_SIZE*xCoord - x; 
    deltaY = TILE_SIZE*yCoord - y;
    
    // Turn
    theta2 = Math.toDegrees(Math.atan2(deltaX, deltaY)); //theta2 now in degrees
    theta1 = odometer.getXYT()[2]; // theta1 in degrees
    
//    LCD.drawString("x " + x, 0, 0);
//    LCD.drawString("y " + y, 0, 1);   
//    LCD.drawString("deltaX " + deltaX, 0, 4);
//    LCD.drawString("deltaY " + deltaY, 0, 5);
    
    
    turnTo(theta2 - theta1);
    
    leftMotor.stop();
    rightMotor.stop();
    
    // Move
    minDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    leftMotor.rotate(convertDistance(minDistance, WHEEL_RAD), true);
    rightMotor.rotate(convertDistance(minDistance, WHEEL_RAD), false);
    
//    LCD.drawString("minDistance: " + minDistance, 0, 6);
//    LCD.drawString("Waypoint: " + xCoord + " " + yCoord, 0, 6);
  }
  
  /*
   * This method causes the robot to turn (on point) to the absolute heading theta. 
   * This method should turn a MINIMAL angle to its target. 
   */
  public void turnTo(double theta) {
    // Convert theta to minimum angle
    if (theta > 180) {
      theta = 360 - theta;
    }
    else if (theta < -180) {
      theta = 360 + theta;
    }
    
    LCD.drawString("turn Angle: " + theta, 0, 3);
    
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