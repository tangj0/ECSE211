package lab1;

import static lab1.Resources.*;

public class PController extends UltrasonicController { //Proportional Controller

  private static final int MOTOR_SPEED = 190; //rpm
  private float speedup;
  private float fastConstant = 1.5f;
  
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
    
    int filterOut = FILTER_OUT_P;
    filter(distance, filterOut, 255, 255); //from UltrasonicController, filters bad values from input

    int error = this.distance - BAND_CENTER_P; 
    speedup = Math.abs(error) * fastConstant;
    
    if (speedup > MOTOR_SPEED/5) {//Limit speedup
      speedup = MOTOR_SPEED/5;
    }
    
    //For a robot moving counter-clockwise
    //Within dead band, go straight
    if (Math.abs(error) <= BAND_WIDTH_P) {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);  
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
      }
    //Too far from wall, go left
    else if (error > 0 && error < 4) {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + (int)2.6*speedup);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    } 
    //Farther than 2nd case, go left more
    else if (error >= 4 && error < 8) {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + (int)2.75*speedup);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
    //Much too far from the wall, go left most
    else if (error > 8){
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + (int)2.95*speedup);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
    //Too close to wall, go right
    else if (error < 0 && error > -10){ 
      LEFT_MOTOR.setSpeed(MOTOR_SPEED + 3*speedup); 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
      } 
    //Error < -10, much too close to wall, backup
    else { 
      LEFT_MOTOR.setSpeed(10); 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + 2*speedup);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.backward();
      }
    
  }


  @Override
  public int readUSDistance() {
    return this.distance;
  }

}
