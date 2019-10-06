package lab4;

import lab4.Odometer;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.Button;

public class Resources {
  
  
  //For Odometer:
  /**
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD = 2.13; 
  
  /**
   * The robot width in centimeters.
   */
  public static final double TRACK = 12.00; //12.20
  
  /**
   * The speed at which the robot moves forward in degrees per second.
   */ 
 public static final float FORWARD_SPEED = 170; //170
  
  
  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 100; //140
  
  /**
   * Timeout period in milliseconds.
   */
  public static final int TIMEOUT_PERIOD = 1000;
  
  /**
   * The tile size in centimeters.
   */
  public static final double TILE_SIZE = 30.48; //30.48
  
  
  //For navigation
  /**
   * Sensor rotation  motor speed 
   */
  public static final int MOTOR_SENSOR_SPEED = 60;
  
  
  //For ultrasonic sensor
  /**
   * Button IDs to use in usLocalizer
   */
  public static final int RISING = Button.ID_LEFT;
  public static final int FALLING = Button.ID_RIGHT;
  
  /**
   * number of sample taken in the median filter (US)
   */
  public static final int MEDIAN_FILTER = 3;
  
  /**
   * threshold to detect a rising edge
   */
  public static final int RISE_THRESHOLD = 40;
  
  /**
   * continue turning after a rising edge
   */
  public static final int RISE_CONTINUE = 25;
  
  /**
   * angle to detect a rising edge
   */
  public static final int RISE_ANGLE = 225;
  
  /**
   * threshold to detect a falling edge
   */
  public static final int FALL_THRESHOLD = 25;
  
  /**
   * continue turning after a falling edge
   */
  public static final int FALL_CONTINUE = 40;
  
  /**
   * angle to detect a falling edge
   */
  public static final int FALL_ANGLE = 45;
  
  /**
   * us localization speed
   */
  public static final int US_SPEED = 100;
  
  
  
  //For light sensor
  /**
   * required change in intensity for light sensor line detection
   */
  public static final double INTENSITY_THRESHOLD = 0.75; ///////////////measure
  
  /**
   * distance from robot's center of rotation to the light sensor
   */
  public static final double LS_DISTANCE = 15.0; ////////////measure
  
  /**
   * Motor rotation speed for light sensor localization
   */
  public static final int LS_SPEED = 100;
  
  
  /**
   * Number of samples taken in the mean filter
   */
  public static final int MEAN_FILTER = 5;
  
  
  //For Main
  /**
   * The LCD screen used for displaying text.
   */
  public static final TextLCD TEXT_LCD = LocalEV3.get().getTextLCD();
  
  
  //Lejos ports
  /**
   * The ultrasonic sensor.
   */
  public static final EV3UltrasonicSensor US_SENSOR = 
      new EV3UltrasonicSensor(LocalEV3.get().getPort("S3"));
  
  /**
   * The color/light sensor.
   */
  public static final EV3ColorSensor L_SENSOR = new EV3ColorSensor(SensorPort.S1);
  
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
   * Sensor rotation motor
   */
  public static final EV3MediumRegulatedMotor sensorMotor =
      new EV3MediumRegulatedMotor(LocalEV3.get().getPort("C"));
  
  /**
   * The LCD.
   */
  public static final TextLCD LCD = LocalEV3.get().getTextLCD();
  
  /**
   * The odometer.
   */
  public static Odometer odometer = Odometer.getOdometer();
 
  
}