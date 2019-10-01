package lab3;

import static lab3.Resources.*;
import static lab3.Navigation.*;

public class BangBangController extends UltrasonicController {
  private int bufferDist;
  
  public BangBangController() {
    bufferDist = 10;  //Backup slightly if bandcenter < distance < bufferDist
  }
  
  @Override
  public void processUSData(int distance) {
    filter(distance);  //from UltrasonicController, filters bad values from input
    
    if (this.distance < BAND_CENTER + bufferDist) {
      Navigation.navigating = false;
      leftMotor.setSpeed(MOTOR_HIGH);
      rightMotor.setSpeed(MOTOR_HIGH);
      leftMotor.forward();
      rightMotor.backward();
    }
    else if (this.distance < BAND_CENTER) {
      Navigation.navigating = false;
      leftMotor.setSpeed(MOTOR_HIGH);
      rightMotor.setSpeed(MOTOR_LOW);
      leftMotor.forward();
      rightMotor.backward();
    }
    else if (this.distance > BAND_CENTER + bufferDist){
      Navigation.navigating = true;
    }
    
  }
  
  
  @Override
  public int readUSDistance() {
    return this.distance;
  }
  
}