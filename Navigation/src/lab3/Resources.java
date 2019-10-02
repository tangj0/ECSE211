package lab3;

import lab3.Odometer;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

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
  
  
  //For Bang Bang Controller
  /**
   * Number of continuous samples the ultrasonic sensor will ignore (filter out) 
   * before measuring the true distance 
   */
  public static final int FILTER_OUT = 10; 
  
  /**
   * Offset from the wall (cm).
   */  
  public static final int BAND_CENTER = 16;
 
  /**
   * Motor speed
   */
  public static final int MOTOR_SPEED = 170; //rpm
  
  public static final int MOTOR_HIGH = 120;
  
  public static final int MOTOR_LOW = 70;
  
  
  
  
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