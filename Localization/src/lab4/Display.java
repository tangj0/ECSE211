package lab4;

import java.text.DecimalFormat;
import lejos.hardware.lcd.LCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

//static import to avoid duplicating variables and make the code easier to read
import static lab4.Resources.*;

/**
 * This class is used to display the content of the odometer variables (x, y, Theta)
 */
public class Display implements TimerListener {

  private double[] position;
  private Timer timer;
  public static final int REFRESH = 100; //display refresh period
 
  
  /**
   * Class constructor
   */
  public Display () {
    LCD.clear();
    this.timer = new Timer(REFRESH, this);
    timer.start();
  }
  
  
  /**
   * Called every time the Timer fires
   * Used to display odometer information
   */
  public void timedOut() {
    // Retrieve x, y and Theta information
    position = odometer.getXYT();
    
    // Print x,y, and theta information
    DecimalFormat numberFormat = new DecimalFormat("######0.00");
    LCD.drawString("X: " + numberFormat.format(position[0]), 0, 0);
    LCD.drawString("Y: " + numberFormat.format(position[1]), 0, 1);
    LCD.drawString("T: " + numberFormat.format(position[2]), 0, 2);
  }
  
  /**
   * Pause display updates
   */
  public void pause() {
    timer.stop();
  }
  
  /**
   * Resume display updates
   */
  public void resume() {
    timer.start();
  }
  
  

}
