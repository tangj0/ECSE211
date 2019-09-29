package lab3;

import static lab3.Resources.*;

//static import to avoid duplicating variables and make the code easier to read
import static lab3.Resources.*;
import static lab3.Main.waypoints;

public class Navigation implements Runnable {
  
  private static double currentTheta;
  
  private static double rotationTheta;
  
  private static double X;
  private static double Y;
  
  private static double distanceX;
  private static double distanceY;
  
  private static double distance;
  
  private static double turnAngle;
  
  private static boolean navigating;
  
  public void run() {
    while (true) {
      for(int i = 0; i < waypoints.length; i++) {
        int xCoord = waypoints[i][0];
        int yCoord = waypoints[i][1];
        travelTo(xCoord, yCoord);
      }
      leftMotor.stop();
      rightMotor.stop();
    }
  }
  
  /*
   * This method returns true if another thread has called travelTo() 
   * or turnTo() and the method has yet to return; false otherwise
   */
  public boolean isNavigating() {
    return navigating;
  }
  
  public void travelTo(double x, double y) {
    navigating = true;
    X = odometer.getXYT()[0]; // gets current X position
    Y = odometer.getXYT()[1]; // gets current Y position
    currentTheta = odometer.getXYT()[2]; // gets current heading
    
    distanceX = x - X; 
    distanceY = y - Y;
    
    turnAngle = Math.atan2(distanceY, distanceX);
    
    distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    
    if (distanceX > 0 && distanceY > 0) {
      turnTo((90 - Math.abs(turnAngle)));
    }
    else if (distanceX > 0 && distanceY < 0) {
      turnTo(Math.abs(turnAngle) + 90);
    }
    else if (distanceX < 0 && distanceY < 0){
     turnTo((90 - Math.abs(turnAngle)) + 180);
    }
    else if (distanceX < 0 && distanceY > 0) {
      turnTo(Math.abs(turnAngle) + 270);
    }
    
    // drive forward distance
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);

    leftMotor.rotate(convertDistance(TILE_SIZE * distance, WHEEL_RAD), true);
    rightMotor.rotate(convertDistance(TILE_SIZE * distance, WHEEL_RAD), false); 
    
    navigating = false;
  }
  
  /*
   * This method causes the robot to turn (on point) to the absolute heading theta. 
   * This method should turn a MINIMAL angle to its target. 
   */
  public void turnTo(double theta) {
    currentTheta = odometer.getXYT()[2]; // gets current heading
    rotationTheta = theta - currentTheta; // calculates the angle the robot must turn
    
    /*
     * Will adjust the angle to determine the shortest angle to turn to the 
     * absolute heading theta
     */
    if (rotationTheta > 180) { 
      rotationTheta -= 360;
    }
    
    if (rotationTheta < 0) { // will turn the robot left depending on the angle
      leftMotor.setSpeed(ROTATE_SPEED); 
      rightMotor.setSpeed(ROTATE_SPEED);
      
      /*
       * takes absolute value of desired angle rotation since sign takes account of direction
       * in the following .rotate 
       */
      rotationTheta = Math.abs(rotationTheta);
      
      leftMotor.rotate(-convertAngle(rotationTheta, WHEEL_RAD), true);
      rightMotor.rotate(convertAngle(rotationTheta, WHEEL_RAD), false);
    }
    else { // will turn the robot right depending on the angle
      leftMotor.setSpeed(ROTATE_SPEED);
      rightMotor.setSpeed(ROTATE_SPEED);
      
      leftMotor.rotate(convertAngle(rotationTheta, WHEEL_RAD), true);
      rightMotor.rotate(-convertAngle(rotationTheta, WHEEL_RAD), false);
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