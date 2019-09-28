package lab3;

import lab3.Odometer;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Resources {
  
  /**
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD = 2.13; 
  
  /**
   * The robot width in centimeters.
   */
  public static final double TRACK = 12.20; 
  
  /**
   * The speed at which the robot moves forward in degrees per second.
   */ 
 public static final float FORWARD_SPEED = 170; 
  
  
  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 140;
  
  /**
   * Timeout period in milliseconds.
   */
  public static final int TIMEOUT_PERIOD = 3000;
  
  /**
   * The tile size in centimeters.
   */
  public static final double TILE_SIZE = 30.48; // 3*TILE_SIZE = 91.44
  
  
  //Number of continuous samples the ultrasonic sensor will ignore (filter out) 
  //before measuring the true distance 
  public static final int FILTER_OUT = 10; 
  
  /**
   * The ultrasonic sensor.
   */
  public static final EV3UltrasonicSensor US_SENSOR = 
      new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));

  /**
   * The left motor.
   */
  public static final EV3LargeRegulatedMotor leftMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));

  /**
   * The right motor.
   */
  public static final EV3LargeRegulatedMotor rightMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
  
  /**
   * The LCD.
   */
  public static final TextLCD LCD = LocalEV3.get().getTextLCD();
  
  /**
   * The odometer.
   */
  public static Odometer odometer = Odometer.getOdometer();
  
}