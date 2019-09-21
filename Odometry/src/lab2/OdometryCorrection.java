package lab2;

import static lab2.Resources.*;

public class OdometryCorrection implements Runnable {
  private static final long CORRECTION_PERIOD = 10;
  public static final int blackThreshold = 100; // Tested value of color blackThreshold
  private float colorData[]; // Array to store sensor data
  private float lastColor; // Stores last sensor value to avoid false positives
  private int lineCount; // Number of lines crossed, from 0 to 8
  private double[] currentPos;  // Adjusted x,y,theta positions to pass into odometer
  private double xOffset, yOffset; //Measured initial x and y offsets from origin

  public OdometryCorrection() {
    colorData = new float[colorSensor.sampleSize()]; // Current color data
    lineCount = 0;
    currentPos = new double[3];
  }

  public void run() {
    long correctionStart, correctionEnd;

    while (true) {
      correctionStart = System.currentTimeMillis();

      // TODO Trigger correction (When do I have information to correct?)

      // TODO Calculate new (accurate) robot position

      // TODO Update odometer with new calculated (and more accurate) values, eg:
      // odometer.setXYT(0.3, 19.23, 5.0);
      
      if (lastColor < blackThreshold) {
        lastColor = colorData[0];
      }
      if (colorData[0] < blackThreshold && lastColor > blackThreshold) {
        lineCount++;
        if (lineCount == 0) {
          currentPos[0] = 0; // Set x
          currentPos[1] = 0; // Set y
          currentPos[2] = 0; // Set theta
        } 
        
        // Set y and theta
        else if (lineCount == 1 || lineCount == 6) {
          currentPos[1] = TILE_SIZE;
          if (lineCount == 1) {
            yOffset = odometer.getXYT()[1]; // Saving y offset
          }
        } 
        else if (lineCount == 2 || lineCount == 5) {
          currentPos[1] = TILE_SIZE * 2; 
          if (lineCount == 5) {
            currentPos[2] = 180;
          }
        } 
        
        // Set x and theta
        else if (lineCount == 3) {
          currentPos[0] = TILE_SIZE;
          currentPos[2] = 90;
          xOffset = odometer.getXYT()[0];
        }
        else if (lineCount == 8) {
          currentPos[0] = xOffset;
          currentPos[1] = yOffset;
          currentPos[2] = 360;
        }
        else if (lineCount == 4 || lineCount == 7) {
          currentPos[0] = TILE_SIZE * 2; 

          if (lineCount == 7) {
            currentPos[2] = 270;
          }
        } 
        else {
          break;
        }
      }
      lastColor = colorData[0];
      odometer.setXYT(currentPos[0], currentPos[1], currentPos[2]);


      // This ensures the odometry correction occurs only once every period
      correctionEnd = System.currentTimeMillis();
      if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
        Main.sleepFor(CORRECTION_PERIOD - (correctionEnd - correctionStart));
      }
    }
  }

}
