package lab3;

import static lab3.Resources.*;

public class PController extends UltrasonicController { //Proportional Controller

  private float speedup;
  private float pConstant = 25; //old=25, 60 = good but almost hitting, 100= a bit too much
                                //200 very fast turns and fast stops, hitting (if stops are registered in time)
  
  public PController() {
    leftMotor.setSpeed(MOTOR_SPEED); // Initialize motor rolling forward
    rightMotor.setSpeed(MOTOR_SPEED);
    leftMotor.forward();
    rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    // Process robot movement based on the distance passed in from the UltrasonicPoller
    // Structure = Main -> UltrasonicPoller -> processUSData()
    
    filter(distance); //from UltrasonicController, filters bad values from input

    int error = this.distance - BAND_CENTER; 
    speedup = (int)(Math.abs(error) * pConstant);
    
    // Filter for false positive (when the distance jumps to a large value, error becomes suddenly large)
    // This increases the speedup variable suddenly (incorrectly). Thus, limit speed to a constant to prevent this
    if (speedup > MOTOR_SPEED/3) {
      speedup = MOTOR_SPEED/3;
    }
    //For a robot moving counter-clockwise
    //Within dead band, go straight
    if (Math.abs(error) <= BAND_WIDTH) {
      leftMotor.setSpeed(MOTOR_SPEED);
      rightMotor.setSpeed(MOTOR_SPEED);  
      leftMotor.forward();
      rightMotor.forward();
      }
    //Too far from wall, go left
    else if (error > 0) {
      leftMotor.setSpeed(MOTOR_SPEED); 
      rightMotor.setSpeed(MOTOR_SPEED + speedup);
      leftMotor.forward();
      rightMotor.forward();
    }
    //Too close to wall, go right
    else if (error < 0 && error > -10){ 
      leftMotor.setSpeed(MOTOR_SPEED + speedup); 
      rightMotor.setSpeed(MOTOR_SPEED);
      leftMotor.forward();
      rightMotor.forward();
      } 
    //Error < -10, much too close to wall, backup
    else { 
      leftMotor.setSpeed(5); 
      rightMotor.setSpeed(MOTOR_SPEED + speedup);
      leftMotor.forward();
      rightMotor.backward();
      }
    
  }


  @Override
  public int readUSDistance() {
    return this.distance;
  }

}
