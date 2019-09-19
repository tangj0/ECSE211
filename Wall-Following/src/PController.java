package lab1;

import static lab1.Resources.*;

public class PController extends UltrasonicController { //Proportional Controller

  private static final int MOTOR_SPEED = 300; //rpm
  private float speedup;
  private float pConstant = 2; //old=25, 60 = good but almost hitting, 100= a bit too much
                                 //200 very fast turns and fast stops, hitting (if stops are registered in time)
  
  public PController() {
    LEFT_MOTOR.setSpeed(MOTOR_SPEED); // Initialize motor rolling forward
    RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
    LEFT_MOTOR.forward();
    RIGHT_MOTOR.forward();
  }

  @Override
  public void processUSData(int distance) {
    // Process robot movement based on the distance passed in from the UltrasonicPoller
    // Structure = Main -> UltrasonicPoller -> processUSData()
    
    filter(distance); //from UltrasonicController, filters bad values from input

    int error = this.distance - BAND_CENTER_P; 
    speedup = (int)(Math.abs(error) * pConstant);
    
    // Filter for false positive (when the distance jumps to a large value, error becomes suddenly large)
    // This increases the speedup variable suddenly (incorrectly). Thus, limit speed to a constant to prevent this
    if (speedup > MOTOR_SPEED/3) {
      speedup = MOTOR_SPEED/3;
    }
    //For a robot moving counter-clockwise
    //Within dead band, go straight
    if (Math.abs(error) <= BAND_WIDTH) {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);  
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
      }
    //Too far from wall, go left
    else if (error > 0) {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED); 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + speedup);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
    //Too close to wall, go right
    else if (error < 0 && error > -10){ 
      LEFT_MOTOR.setSpeed(MOTOR_SPEED + speedup); 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
      } 
    //Error < -10, much too close to wall, backup
    else { 
      LEFT_MOTOR.setSpeed(5); 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + speedup);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.backward();
      }
    
  }


  @Override
  public int readUSDistance() {
    return this.distance;
  }

}
