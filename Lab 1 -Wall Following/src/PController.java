package ca.mcgill.ecse211.lab1;

import static ca.mcgill.ecse211.lab1.Resources.*;

public class PController extends UltrasonicController { //Proportional Controller

  private static final int MOTOR_SPEED = 150; //rpm
  
  private int distance; //Current distance from wall
  private int filterControl; //Sensor sample counter
  private int speedup;
  private int errorConstant = 0.5;
  
  public PController() {
    LEFT_MOTOR.setSpeed(MOTOR_SPEED); // Initialize motor rolling forward
    RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
    LEFT_MOTOR.forward();
    RIGHT_MOTOR.forward();
 
    filterControl = 0;
  }

  @Override
  public void processUSData(int inputDistance) {
    // Process robot movement based on the distance passed in from the UltrasonicPoller
    // Structure = Main -> UltrasonicPoller -> processUSData()
    
    filter(inputDistance); //from UltrasonicController, filters bad values from input

    int error = Math.abs(BAND_CENTER - this.distance); 
    this.speedup = error * this.errorConstant * MOTOR_SPEED;
    
    //For a robot moving counter-clockwise
    //Within deadband, go straight
    if (Math.abs(error <= BAND_WIDTH)) {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
    //Too far from wall, veer left
    else if (error < 0) { 
      LEFT_MOTOR.setSpeed(MOTOR_SPEED) 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + speedup);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    } 
    //Too close to wall (error > 0) , veer right
    else {
      LEFT_MOTOR.setSpeed(MOTOR_SPEED + speedup) 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
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
