package lab4;
import static lab4.Resources.*;
import java.util.Arrays;
import static lab4.Navigation.*;
import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;

public class UltrasonicLocalizer extends Thread {
  private int edgeType; 
  private SampleProvider usSampleProvider;
  private float[] usData;
  

  public UltrasonicLocalizer(int buttonID, SampleProvider usSampleProvider, float[] usData) {
    this.edgeType = buttonID;
    this.usSampleProvider = usSampleProvider;
    this.usData = usData;
    
    leftMotor.setSpeed(US_SPEED);
    rightMotor.setSpeed(US_SPEED);
  }
  
  public void run() { 
    double dTheta;
    
    if (edgeType == RISING) {
      dTheta = risingEdge(); 
    }
    else if (edgeType == FALLING) {
      dTheta = fallingEdge();
    }
    else {
      dTheta = -1;
    }
    
    odometer.setTheta(dTheta);
    
    // physically turn robot to a heading of 0 degrees 
    turnLeft(dTheta);
  }
  
  
  /**
   * Used when robot is facing towards the wall. 
   * The robot turns till it no longer see the wall (rising edge) and repeats on the other side
   * @return
   */
  private double risingEdge() {
    double theta1, theta2; //angles of first and second rising edge
    while (medianFilter() < RISE_THRESHOLD) {
      turnRight();
    }
    
    Sound.beep();
    stopMotors();
    
    theta1 = odometer.getXYT()[2];
    
    turnLeft(40);
    
    while(medianFilter() < RISE_THRESHOLD) {
      turnLeft();
    }
    
    Sound.beep();
    stopMotors();
    theta2 = 360 - odometer.getXYT()[2];
    
    return RISE_ANGLE - (theta1 + theta2)/2;
  }
  
  /**
   * Used when robot is facing way from wall.
   * The robot turns till it sees the wall (falling edge) and repeats on the other side
   * @return
   */
  private double fallingEdge() {
    double theta1, theta2; //angles of first and second rising edge
    while (medianFilter() > FALL_THRESHOLD) {
      turnLeft();
    }
    
    Sound.beep();
    stopMotors();
    
    theta1 = 360 - odometer.getXYT()[2];
    
    turnRight(40);
    
    while(medianFilter() > FALL_THRESHOLD) {
      turnRight();
    }
    
    Sound.beep();
    stopMotors();
    theta2 = odometer.getXYT()[2];
    
    return FALL_ANGLE + (theta1 + theta2)/2;
  }
  
  
  
  /**
   * helper method
   */
  private void stopMotors() {
    leftMotor.stop();
    rightMotor.stop();
  }
  
  /**
   * helper method
   */
  private void turnRight() {
    leftMotor.forward();
    rightMotor.backward();
  }
  
  /**
   * helper method
   */
  private void turnLeft() {
    leftMotor.backward();
    rightMotor.forward();
  }
  
  /**
   * helper method
   */
  private void turnLeft(double angle) {
    rightMotor.rotate(Navigation.convertAngle(angle, WHEEL_RAD), true);
    leftMotor.rotate(-Navigation.convertAngle(angle, WHEEL_RAD), false);
  }
  
  /**
   * helper method
   */
  private void turnRight(double angle) {
    rightMotor.rotate(-Navigation.convertAngle(angle, WHEEL_RAD), true);
    leftMotor.rotate(Navigation.convertAngle(angle, WHEEL_RAD), false);
  }
  
  /**
   * @param distance
   * @return the median of MEDIAN_FILTER samples
   */
  private double medianFilter () {
    double[] tempData = new double[MEDIAN_FILTER];
    int MAX = 255;
    for (int i = 0; i < MEDIAN_FILTER; i++) {
      this.usSampleProvider.fetchSample(usData, 0);
      tempData[i] = usData[0] * 100.0; ;
    }
    Arrays.sort(tempData);
    if (tempData[MEDIAN_FILTER/2] > MAX) {
      return MAX;
    }
    return tempData[MEDIAN_FILTER/2]; //return median
  }
  
  
  
}