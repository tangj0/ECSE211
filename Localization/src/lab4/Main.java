package lab4;

import static lab4.Resources.*;
import lejos.robotics.SampleProvider;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;


public class Main {
  public static Display display;
  public static void main(String[] args) {
    
  SampleProvider usSampleProvider = US_SENSOR.getMode("Distance");
  float[] usData = new float[usSampleProvider.sampleSize()];
 
  SampleProvider lsSampleProvider = L_SENSOR.getMode("Red");
  float[] lsData = new float[lsSampleProvider.sampleSize()];
    
    
    int buttonChoice;
    do {
      LCD.clear();
      LCD.drawString("< Left  |  Right >", 0, 0);
      LCD.drawString(" Rising | Falling ", 0, 1);
      LCD.drawString("  Edge  |  Edge   ", 0, 2);
      buttonChoice = Button.waitForAnyPress();

    } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT && buttonChoice != Button.ID_ESCAPE);


    if (buttonChoice == Button.ID_LEFT || buttonChoice == Button.ID_RIGHT) {
      UltrasonicLocalizer usLoc = new UltrasonicLocalizer(buttonChoice, usSampleProvider, usData);
      Display display = new Display();
      
      // create threads
      Thread odoThread = new Thread(odometer);
      Thread usLocalizerThread = new Thread(usLoc);
      Thread displayThread = new Thread(display);
      
      // start odometer, display, and ultrasonic localization
      odoThread.start();
      displayThread.start();
      usLocalizerThread.start(); 

      // Allows verification of ultrasonic localization before starting light localization
      if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
        System.exit(0); //exists if esc button is pressed
        
      } else {
        // start light localization 
        LightLocalizer lightloc = new LightLocalizer(lsSampleProvider, lsData);
        Thread lightlocThread = new Thread(lightloc);
        lightlocThread.start();

      }
    }
    else {
      System.exit(0);
    }
    
    while(Button.waitForAnyPress() != Button.ID_ESCAPE) {
      // keep program from ending unless esc is pressed
    }
    System.exit(0);

  }
    
    
}
  
  
  
  
  
  
  
  
  