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
    int error = distance - BAND_CENTER;
    int filterOut = FILTER_OUT;

    filter(distance, filterOut, 125, 175);

    // TODO: process a movement based on the us distance passed in (BANG-BANG style)

    if (Math.abs(error) <= BAND_WIDTH) {
      LEFT_MOTOR.setSpeed(MOTOR_HIGH); // Start robot moving forward
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }    
    // If robot is too far from the wall, increase speed for outside wheel decrease speed for inside wheel
    else if (error > 0) {
      LEFT_MOTOR.setSpeed(MOTOR_LOW); // Decrease speed inside wheel
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH); // Increase speed outside wheel
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }  
    // If robot is too close from the wall, we must increase speed of inside wheel and decrease speed of outside wheel
    else if (error < 0 && error > -20) {
      LEFT_MOTOR.setSpeed(MOTOR_HIGH);
      RIGHT_MOTOR.setSpeed(MOTOR_LOW);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
    else if (error <= -20) {
      LEFT_MOTOR.setSpeed(10); 
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH);
      LEFT_MOTOR.backward();
      RIGHT_MOTOR.backward();
    }
    // The robot can continue on the same path because it isn't too close or too far from wall
    else {
      LEFT_MOTOR.setSpeed(MOTOR_HIGH);
      RIGHT_MOTOR.setSpeed(MOTOR_HIGH);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }

  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
