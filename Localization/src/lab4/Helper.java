package lab4;

import static lab4.Resources.*;

public class Helper{
  
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
   * Helper method, moves motors forward
   */
  public static void moveForward() {
    leftMotor.forward();
    rightMotor.forward();
  }
  
  /**
   * Helper method, moves motors forward
   * @param distance  forward distance
   */
  public static void moveForward(double distance) {
    leftMotor.rotate(convertDistance(distance, WHEEL_RAD), true);
    rightMotor.rotate(convertDistance(distance, WHEEL_RAD), false);
  }
  
  /**
   * Helper method, moves motors backward
   */
  public static void moveBackward() {
    leftMotor.backward();
    rightMotor.backward();
  }
  
  /**
   * Helper method, moves motors backward
   * @param distance  backward distance
   */
  public static void moveBackward(double distance) {
    leftMotor.rotate(-convertDistance(distance, WHEEL_RAD), true);
    rightMotor.rotate(-convertDistance(distance, WHEEL_RAD), false);
  }
  
  /**
   * Helper method, turns motors right
   */
  public static void turnRight() {
    leftMotor.forward();
    rightMotor.backward();
  }
  
  /**
   * Helper method, turns motors right
   * @param angle  turn angle
   */
  public static void turnRight(double angle) {
    leftMotor.rotate(convertAngle(angle, WHEEL_RAD), true);
    rightMotor.rotate(-convertAngle(angle, WHEEL_RAD), false);
  }
  
  /**
   * Helper method, turns motors left
   */
  public static void turnLeft() {
    leftMotor.backward();
    rightMotor.forward();
  }
  
  /**
   * Helper method, turns motors left
   * @param angle  turn angle
   */
  public static void turnLeft(double angle) {
    leftMotor.rotate(-convertAngle(angle, WHEEL_RAD), true);
    rightMotor.rotate(convertAngle(angle, WHEEL_RAD), false);
  }
  
  /**
   * Helper method, stops both motors
   */
  public static void stopMotors() {
    leftMotor.stop();
    rightMotor.stop();
  }
  
  
}