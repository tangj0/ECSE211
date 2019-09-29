package lab3;

import lejos.hardware.Button;
import static lab3.Resources.*;

public class Main {
  
  public static UltrasonicController selectedController;
  public static int[][] waypoints;
  private static boolean avoid;
  
  public static void main(String[] args) {
//    waypoints = new int[5][2];
//    
//    
//    //waypoints from map 1
//    waypoints[0] = new int[] {1,3};
//    waypoints[1] = new int[] {2,2};
//    waypoints[2] = new int[] {3,3};
//    waypoints[3] = new int[] {3,2};
//    waypoints[4] = new int[] {2,1};

    
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
    // Display, navigation, poller threads
    new Thread(new Display()).start();
    new Thread(new Navigation()).start();

    if (avoid) {
      selectedController = new PController(); 
      new Thread(new UltrasonicPoller()).start();
    }
    
    while (Button.waitForAnyPress() != Button.ID_ESCAPE) {
    } // do nothing
  
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

