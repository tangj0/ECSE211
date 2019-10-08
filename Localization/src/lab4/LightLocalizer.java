package lab4;
import static lab4.Resources.*;
import lab4.Navigation;
import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;

/**
 * Light sensor localization class
 */
public class LightLocalizer extends Thread {
  private double intensity, initialReading;
  
  private double theta1, theta2, theta3, theta4; //angles recorded at the 1-4 lines
  
  private SampleProvider lsSampleProvider;
  private float[] lsData;
  
  
  /**
   * Assumes the robot starts at theta = 0 in a position close enough to (1,1)
   * Where a 360 degree rotation allows the sensor to capture all four gridlines
   */
  public LightLocalizer(SampleProvider lsSampleProvider, float[] lsData) {
    this.lsSampleProvider = lsSampleProvider;
    this.lsData = lsData;
    
    leftMotor.setSpeed(LS_SPEED);
    rightMotor.setSpeed(LS_SPEED);
  }
  

  public void run() {
    initialReading = meanFilter();
    
    // initial position adjustment to move closer to origin
    turnRight(90);
    moveBackward(2.5);
    forwardTillLine();
    moveForward(LS_DISTANCE/2);
    
    turnLeft(90);
    moveBackward(2.5);
    forwardTillLine();
    moveForward(LS_DISTANCE/2);
    odometer.setTheta(0);
    findPoints();
    
    //calculate x and y offsets from origin
    double thetaY = theta3 - theta1;
    double thetaX = theta1 + theta2 + (360-theta4);
    
    double dy = LS_DISTANCE*Math.cos(thetaX/2*Math.PI/180);
    double dx = LS_DISTANCE*Math.cos(thetaY/2*Math.PI/180);
    
    odometer.setY(-dy);
    odometer.setX(-dx);
    
    LCD.drawString("delta x: " + dx, 0, 3);
    LCD.drawString("delta y: " + dy, 0, 4);
    
    // Move robot's center of rotation of (0,0) and turn to 0 degrees
    turnRight(90);
    moveForward(dx);
    turnLeft(90);
    moveForward(dy);
  }
  
  private void findPoints() {
    // both motors non-blocking to allow gridline detection while turning
    leftMotor.rotate(Navigation.convertAngle(360, WHEEL_RAD), true);
    rightMotor.rotate(-Navigation.convertAngle(360, WHEEL_RAD), true);
    
    //robot turns clockwise, records 4 angles  
    theta1 = recordAngle();
    theta2 = recordAngle();
    theta3 = recordAngle();
    theta4 = recordAngle();
    
    // Wait for robot to finish turning
    while (leftMotor.isMoving() || rightMotor.isMoving()) {
      // wait
      try {
        Thread.sleep(50);
      } catch (Exception e) {
        // nothing
      }

    }
  }
  
  private double meanFilter () {
    int sum = 0;
    for (int i = 0; i < MEAN_FILTER; i++) {
      lsSampleProvider.fetchSample(lsData, 0);
      sum += lsData[0]*100;
    }
    return sum/MEAN_FILTER; //return median
  }

  private double recordAngle() {
    while(leftMotor.isMoving() || rightMotor.isMoving()) {
      intensity = meanFilter();
      if (intensity/initialReading < INTENSITY_THRESHOLD) {
        Sound.beep();
        return odometer.getXYT()[2]; //get the angle at a line
      }
    }
    return -1;
  }
  
  private void forwardTillLine() {
    while (true) {
      intensity = meanFilter();
      // line detected
      if (intensity/initialReading < INTENSITY_THRESHOLD) {
         Sound.beep();
         break;
      }
      moveForward(); // move forward until line detected
    } 
  }
  
  private void moveForward() {
    leftMotor.forward();
    rightMotor.forward();
  }
  
  private void moveForward(double distance) {
    leftMotor.rotate(Navigation.convertDistance(distance, WHEEL_RAD), true);
    rightMotor.rotate(Navigation.convertDistance(distance, WHEEL_RAD), false);
  }
  
  private void moveBackward(double distance) {
    leftMotor.rotate(-Navigation.convertDistance(distance, WHEEL_RAD), true);
    rightMotor.rotate(-Navigation.convertDistance(distance, WHEEL_RAD), false);
  }
  
  /**
   * helper method
   */
  private void turnRight(double angle) {
    leftMotor.rotate(Navigation.convertAngle(angle, WHEEL_RAD), true);
    rightMotor.rotate(-Navigation.convertAngle(angle, WHEEL_RAD), false);
  }
  
  
  /**
   * helper method
   */
  private void turnLeft(double angle) {
    leftMotor.rotate(-Navigation.convertAngle(angle, WHEEL_RAD), true);
    rightMotor.rotate(Navigation.convertAngle(angle, WHEEL_RAD), false);
  }
  
  private int recordTacho() {
    return (leftMotor.getTachoCount() + rightMotor.getTachoCount()) / 2;
  }
}