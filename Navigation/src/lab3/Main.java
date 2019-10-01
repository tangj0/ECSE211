package lab3;

import lejos.hardware.Button;
import static lab3.Resources.*;

public class Main {
  public static UltrasonicController selectedController;
  private static boolean avoid;
  public static int[][] waypoints;

  public static void main(String[] args) {
    waypoints = new int[5][2]; 
    
    waypoints[0] = new int[] {1,3};
    waypoints[1] = new int[] {2,2};
    waypoints[2] = new int[] {3,3};
    waypoints[3] = new int[] {3,2};
    waypoints[4] = new int[] {2,1};

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

    
    // Poller thread, naming it to be used later, 
    if (avoid) {
      selectedController = new BangBangController();  
      new Thread(new UltrasonicPoller()).start();
    }
    
    // reset motos and set odometer
    leftMotor.stop();
    rightMotor.stop();
    odometer.setXYT(TILE_SIZE, TILE_SIZE, 0);
    Navigation.navigating = true;
    
    for(int i = 0; i < waypoints.length; i++) {
      int xCoord = waypoints[i][0];
      int yCoord = waypoints[i][1];
      Navigation.travelTo(xCoord, yCoord); //directly using the static method    
    }
    
    while (Button.waitForAnyPress() != Button.ID_ESCAPE) {
    }// do nothing
     
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

