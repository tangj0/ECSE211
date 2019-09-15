package lab1;

import static lab1.Resources.*;

public class PController extends UltrasonicController { //Proportional Controller

  private static final int MOTOR_SPEED = 200; //rpm
  private float speedup;
  private float errorConstant = 0.01f;
  
  public PController() {
    LEFT_MOTOR.setSpeed(MOTOR_SPEED); // Initialize motor rolling forward
    RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
    LEFT_MOTOR.forward();
    RIGHT_MOTOR.forward();
 
    //filterControl = 0;
  }

  @Override
  public void processUSData(int distance) {
    // Process robot movement based on the distance passed in from the UltrasonicPoller
    // Structure = Main -> UltrasonicPoller -> processUSData()
    
    filter(distance); //from UltrasonicController, filters bad values from input

    int error = BAND_CENTER - this.distance; 
    this.speedup = Math.abs(error) * this.errorConstant * MOTOR_SPEED;
    
    //For a robot moving counter-clockwise
    //Within dead band, go straight
    if (Math.abs(error) <= BAND_WIDTH) {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
    //Too far from wall, veer left
    else if (error < 0) { 
      if (error > -5) { //far from wall but not too far
        LEFT_MOTOR.setSpeed(MOTOR_SPEED);
        RIGHT_MOTOR.setSpeed(MOTOR_SPEED + speedup/2);
        LEFT_MOTOR.forward();
        RIGHT_MOTOR.forward();
      } else {
        LEFT_MOTOR.setSpeed(MOTOR_SPEED);
        RIGHT_MOTOR.setSpeed(MOTOR_SPEED + speedup);
        LEFT_MOTOR.forward();
        RIGHT_MOTOR.forward();
      }
    } 
    //Too close to wall (error > 0) , veer right
    else {
      if (error < 5) {
        LEFT_MOTOR.setSpeed(MOTOR_SPEED + speedup/2); 
        RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
        LEFT_MOTOR.forward();
        RIGHT_MOTOR.forward();
      } else {
        LEFT_MOTOR.setSpeed(MOTOR_SPEED + speedup); 
        RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
        LEFT_MOTOR.forward();
        RIGHT_MOTOR.forward();
      }
    }
    
    //Let other processes access cpu after program completion
    try {
      Thread.sleep(50);
    } catch(Exception e){
      e.printStackTrace();
    }
    
  }


  @Override
  public int readUSDistance() {
    return this.distance;
  }

}
