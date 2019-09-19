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
    
    filter(distance);
    
    int error = BAND_CENTER - (int)(this.distance * Math.sqrt(2)/2); //taking cos of 45 times distance because sensor was on 45 degree angle
    //int error = BAND_CENTER - this.distance;
    
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
    // If robot is too close to the wall, we must increase speed of inside wheel and decrease speed of outside wheel
    else {
      LEFT_MOTOR.setSpeed(MOTOR_HIGH);
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.backward();
    }
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
