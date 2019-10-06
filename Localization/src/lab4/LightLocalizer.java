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
  
  private double leftX, rightX, upY, downY;
  
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
    int startTacho = recordTacho();
    
    while (true) {
      intensity = meanFilter();
      // line detected
      if (intensity/initialReading < INTENSITY_THRESHOLD) {
         Sound.beep();
         break;
      }
      moveForward(); // move forward until line detected
    } 
    stopMotors();
    
    // initial placement adjustments
    int endTacho = recordTacho();
    int distanceToLine = Navigation.convertAngle(endTacho - startTacho, WHEEL_RAD);
    moveBackward(distanceToLine);
    turnRight(45); //turn to face origin
    moveForward(distanceToLine - LS_DISTANCE);
    turnLeft(45); //turn straight
    odometer.setTheta(0);
    
    findPoints();
    
    // calculate current y position and update odometer
    double xTheta = Math.abs(leftX - rightX) / 2.0;
    double dy = Math.cos(xTheta * Math.PI/180) * LS_DISTANCE;
    odometer.setY(-dy);
    
    // calculate current x position and update odometer
    double yTheta = Math.abs(downY - upY) / 2.0;   
    // to prevent using an angle > 180 due to odometer inaccuracy 
    // angle theoretically should be < 180
    if (yTheta > 90) {
      yTheta = 180 - yTheta;
    }
    double dx = Math.cos(yTheta * Math.PI/180) * LS_DISTANCE;
    odometer.setX(-dx);
    
    Navigation.travelTo(1,1);  
    Navigation.turnTo(0);
  }
  
  private void findPoints() {
    // both motors non-blocking to allow gridline detection while turning
    leftMotor.rotate(Navigation.convertAngle(360, WHEEL_RAD), true);
    rightMotor.rotate(-Navigation.convertAngle(360, WHEEL_RAD), true);
    
    // robot turns counter clockwise, record 4 distances
    leftX = recordAngle();
    upY = recordAngle();
    rightX = recordAngle();
    downY = recordAngle();
    
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
  
  private void moveForward() {
    leftMotor.forward();
    rightMotor.forward();
  }
  
  private void moveForward(double distance) {
    leftMotor.rotate(Navigation.convertDistance(distance, WHEEL_RAD), true);
    leftMotor.rotate(Navigation.convertDistance(distance, WHEEL_RAD), false);
  }
  
  private void moveBackward(double distance) {
    leftMotor.rotate(Navigation.convertDistance(-distance, WHEEL_RAD), true);
    rightMotor.rotate(Navigation.convertDistance(-distance, WHEEL_RAD), false);
  }
  
  private void stopMotors() {
    leftMotor.stop();
    rightMotor.stop();
  }
  
  /**
   * helper method
   */
  private void turnRight(double angle) {
    rightMotor.rotate(-Navigation.convertAngle(angle, WHEEL_RAD), true);
    leftMotor.rotate(Navigation.convertAngle(angle, WHEEL_RAD), false);
  }
  
  /**
   * helper method
   */
  private void turnLeft(double angle) {
    rightMotor.rotate(Navigation.convertAngle(angle, WHEEL_RAD), true);
    leftMotor.rotate(-Navigation.convertAngle(angle, WHEEL_RAD), false);
  }
  
  private int recordTacho() {
    return (leftMotor.getTachoCount() + rightMotor.getTachoCount()) / 2;
  }
}