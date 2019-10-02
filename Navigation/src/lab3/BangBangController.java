package lab3;

import static lab3.Resources.*;

public class BangBangController extends UltrasonicController{
  private boolean stopWaving;
  private int sensorAngle;
  
  /**
   * class constructor
   */
  public BangBangController () {
    stopWaving = false;  //sensor stops waving when obstacle is seen
  }
  
  /**
   * @param theta  angle of sensor motor rotation
   */
  public void waveSensor(int theta) {
    // start the sensor (hardware) turned to the left
    sensorMotor.setSpeed(MOTOR_SENSOR_SPEED);
    if (!stopWaving) {
      sensorMotor.rotate((int)Math.PI*(theta)/180); //turns right
      sensorMotor.rotate((int)Math.PI*-theta/180);  //turns left
    }
 }
  
  /**
   * Perform an action based on the US data input.
   * 
   * @param distance the distance to the wall in cm
   */
  @Override
  public void processUSData(int distance) {
    filter(distance);  //from UltrasonicController, filters bad values from input
    waveSensor(6500); // Move sensor back and forth for better accuracy
    
    LCD.drawString("distance: " +  this.distance, 0, 4);
    
    if (this.distance < BAND_CENTER ) {
      Navigation.navigating = false;
      stopWaving = true;
      
      leftMotor.setSpeed(MOTOR_HIGH);
      rightMotor.setSpeed(MOTOR_HIGH);      
      leftMotor.rotate(20);
      rightMotor.rotate(-40);
      Navigation.navigating = true;
    }
    stopWaving = false;
  }
  
  /**
   * Returns the distance between the US sensor and an obstacle in cm.
   * 
   * @return the distance between the US sensor and an obstacle in cm
   */
  @Override
  public int readUSDistance() {
    return this.distance;
  }
}