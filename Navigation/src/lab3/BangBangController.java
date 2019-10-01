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
    }
    else if (this.distance < BAND_CENTER) {
      Navigation.navigating = false;
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