package lab3;

import lejos.hardware.Button;
import static lab3.Resources.*;

public class Main {
  
  public static UltrasonicController selectedController;
  public static int[][] waypoints;
  private static boolean avoid;
  
  public static void main(String[] args) {
    waypoints = new int[5][2];
    
    //waypoints from map 1
    waypoints[0] = new int[] {1,3};
    waypoints[1] = new int[] {2,2};
    waypoints[2] = new int[] {3,3};
    waypoints[3] = new int[] {3,2};
    waypoints[4] = new int[] {2,1};
    
    selectedController = new PController(); 
    
    
    int option = Button.waitForAnyPress();
    
    Display.showText("<  Left   |   Right >",
                     "    No    |   With  >",
                     " Obstacle | Obstacle",
                     "                    ");
    
    // Button choice determines driving with or without obstacle avoidance = PController
    if (option == Button.ID_LEFT) {
      avoid = false;
    }
    else if (option == Button.ID_RIGHT) {
      avoid = true;
    }
    else {
      TEXT_LCD.clear();
      System.err.println("Error - Invalid button!");
      // Sleep for 2 seconds so user can read error message
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
      }
      System.exit(-1);
    }
    
    // Start the odometer, poller (for pcontroller), navigation and display threads
    new Thread(odometer).start();
    new Thread(new Navigation()).start();
    if (avoid) {
      new Thread(new UltrasonicPoller()).start();
    }
    new Thread(new Display()).start();
    
    // Wait here until any button is pressed to terminate
    Button.waitForAnyPress();
    System.exit(0);

  }
  
  
  
  
  
}

