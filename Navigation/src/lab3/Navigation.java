package lab3;

import static lab3.Resources.*;

//static import to avoid duplicating variables and make the code easier to read
import static lab3.Resources.*;
import static lab3.Main.waypoints;

public class Navigation implements Runnable {
  
  private static double currentTheta;
  private static double rotationTheta;
  
  private static double x, y; // these are the current coordinates of the robot
  private static double deltaX, deltaY;
  
  private static double minDistance;
  private static double theta1, theta2; //theta1 is current heading, theta 2 is desired heading
  
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
    // Reset motors and odometer
    leftMotor.stop();
    rightMotor.stop();
    odometer.setXYT(1, 1, 0);

    for(int i = 0; i < waypoints.length; i++) {
      int xCoord = waypoints[i][0];
      int yCoord = waypoints[i][1];
      travelTo(xCoord, yCoord);        
      LCD.drawString(Integer.toString(xCoord), 0, 6);
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
    // Gets current x, y positions and convert from cm to integer
    x = Math.round(odometer.getXYT()[0]/TILE_SIZE);  
    y = Math.round(odometer.getXYT()[1]/TILE_SIZE); 
    
    deltaX = TILE_SIZE*(xCoord - x); //xCoord is the coordinate of the next waypoint
    deltaY = TILE_SIZE*(yCoord  - y); //yCoord is the coordinate of the next waypoint
    
    // Turn
    theta2 = Math.toDegrees(Math.atan2(deltaX, deltaY)); //theta2 (desired heading) now in degrees
  
    turnTo(theta2);
    //Navigation.sleepFor(TIMEOUT_PERIOD);
    leftMotor.stop();
    rightMotor.stop();
    // Move
    minDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    leftMotor.rotate(convertDistance(minDistance, WHEEL_RAD), true);
    rightMotor.rotate(convertDistance(minDistance, WHEEL_RAD), false);
    
    LCD.drawString("x " + x, 0, 4);
    LCD.drawString("y " + y, 0, 5);
    LCD.drawString("minDistance: " + minDistance, 0, 6);
  }
  
  /*
   * This method causes the robot to turn (on point) to the absolute heading theta. 
   * This method should turn a MINIMAL angle to its target. 
   */
  public void turnTo(double theta) {
    theta1 = odometer.getXYT()[2]; // theta1 in degrees
    // Convert theta to minimum angle
    if (Math.abs(theta - theta1) < 180) {
      if (theta - theta1 < 0) {
        theta = theta + 180;
      }
      else {
        theta = theta - 180;
      }
    }
    
    LCD.drawString("turn Angle: " + theta, 0, 3);
    
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    leftMotor.rotate(convertAngle((theta - theta1), WHEEL_RAD), true);
    rightMotor.rotate(-convertAngle((theta - theta1), WHEEL_RAD), false);

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