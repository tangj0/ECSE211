package lab3;

import lejos.hardware.Button;
import static lab3.Resources.*;

public class Main {
  public static UltrasonicController selectedController;
  private static boolean avoid;
  public static int[][] waypoints;
  public static Navigation myNav;

  public static void main(String[] args) {

    int buttonChoice;
    new Thread(odometer).start(); //Odometer thread
    Display.showText("< Left  |   Right >",
                     "   No   |   With  >",
                     "Obstacle| Obstacle ",
                     "                    ");
    
    do {
      buttonChoice = Button.waitForAnyPress();
    } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
    
    
    // Button choice determines driving with or without obstacle avoidance = PController
    if (buttonChoice == Button.ID_LEFT) {
      avoid = false;
    }
    else if (buttonChoice == Button.ID_RIGHT) {
      avoid = true;
    }
    new Thread(new Display()).start(); //Display thread

    if (avoid) {
      //poller thread
      //selectedController = new Navigation();
      new Thread(new UltrasonicPoller()).start();
    } 
    else {
      //navigation thread
      new Thread(new Navigation()).start();
    }
   
    while (Button.waitForAnyPress() != Button.ID_ESCAPE) {
    } //do nothing
    
    System.exit(0);

  }
  
  
  /**
   * Sleeps current thread for the specified duration.
   * 
   * @param duration sleep duration in milliseconds
   */
  public static void sleepFor(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      // There is nothing to be done here
    }
  }
  
  
}

