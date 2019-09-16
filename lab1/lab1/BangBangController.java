package lab1;

import static lab1.Resources.*;

public class BangBangController extends UltrasonicController {

  public BangBangController() {
    LEFT_MOTOR.setSpeed(MOTOR_HIGH); // Start robot moving forward
    RIGHT_MOTOR.setSpeed(MOTOR_HIGH);
    LEFT_MOTOR.forward();
    RIGHT_MOTOR.forward();
  }
   
  @Override
  public void processUSData(int distance) {
    int error = BAND_CENTER - this.distance;
    int filterOut = FILTER_OUT;

    filter(distance, filterOut, 60, 150);

    // TODO: process a movement based on the us distance passed in (BANG-BANG style)

    if (Math.abs(error) <= BAND_WIDTH) {
      LEFT_MOTOR.setSpeed(MOTOR_HIGH); // Start robot moving forward
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }    
    // If robot is too far from the wall, increase speed for outside wheel decrease speed for inside wheel
    else if (error < 0) {
      LEFT_MOTOR.setSpeed(MOTOR_LOW); // Decrease speed inside wheel
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH); // Increase speed outside wheel
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }  
    // If robot is too close from the wall, we must increase speed of inside wheel and decrease speed of outside wheel
    else if (error > 0) {
      LEFT_MOTOR.setSpeed(MOTOR_HIGH);
      RIGHT_MOTOR.setSpeed(MOTOR_LOW);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
    // The robot can continue on the same path because it isn't too close or too far from wall
    else {
      LEFT_MOTOR.setSpeed(MOTOR_HIGH);
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
 
    try{
      Thread.sleep(50); 
      //After program is run, the thread is told to sleep so other processes can access CPU
      }
    catch(Exception e){
    e.printStackTrace(); 
    } 
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
