package lab1;

import static lab1.Resources.*;

/**
 * Controller that controls the robot's movements based on ultrasonic data.
 */
public abstract class UltrasonicController {
  
  int distance;
  
  int filterControl;
    
  /**
   * Perform an action based on the US data input.
   * 
   * @param distance the distance to the wall in cm
   */
  public abstract void processUSData(int distance);

  /**
   * Returns the distance between the US sensor and an obstacle in cm.
   * 
   * @return the distance between the US sensor and an obstacle in cm
   */
  public abstract int readUSDistance();
  
  /**
   * Rudimentary filter - toss out invalid samples corresponding to null signal.
   * @param distance distance in cm
   */
  
  void filter(int distance, int filterOutNum, int num1, int num2) {
    if (distance >= num1 && filterControl < filterOutNum) {
      // bad value, do not set the distance var, however do increment the filter value
      filterControl++;
    } else if (distance >= num2) {
      // Repeated large values, so there is nothing there: leave the distance alone
      this.distance = (int)(distance * Math.sqrt(2)/2); 
    } else {
      // distance went below 255: reset filter and leave distance alone.
      filterControl = 0;
      this.distance = (int)(distance * Math.sqrt(2)/2);
    }
  }
  
}
