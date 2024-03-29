package lab3;

import static lab3.Resources.*;

/**
 * Samples the US sensor and invokes the selected controller on each cycle.
 * 
 * Control of the wall follower is applied periodically by the UltrasonicPoller thread. The while
 * loop at the bottom executes in a loop. Assuming that the us.fetchSample, and cont.processUSData
 * methods operate in about 20ms, and that the thread sleeps for 50 ms at the end of each loop, then
 * one cycle through the loop is approximately 70 ms. This corresponds to a sampling rate of 1/70ms
 * or about 14 Hz.
 */
public class UltrasonicPoller implements Runnable {

  private UltrasonicController controller;
  private float[] usData;

  /**
   * Class constructor
   */
  public UltrasonicPoller() {
    leftMotor.stop();
    rightMotor.stop();
    usData = new float[US_SENSOR.sampleSize()];
    controller = Main.selectedController;
  }

  /**
   * Runs the logic of the UltrasonicPoller class
   */
  public void run() {
    int distance;
    while (true) {
      US_SENSOR.getDistanceMode().fetchSample(usData, 0); // acquire distance data in meters
      distance = (int) (usData[0] * 100.0); // extract from buffer, convert to cm, cast to int
      controller.processUSData(distance); // now take action depending on value
      try {
        Thread.sleep(25);
      } catch (Exception e) {
      } // Poor man's timed sampling
    }
  }

}
