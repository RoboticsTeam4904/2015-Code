/* UART Example, any character received on either the real
 serial port, or USB serial (or emulated serial to the
 Arduino 
 Monitor when using non-serial USB types)
 is printed as a message to both ports.
 
 This example code is in the public domain.
 */



unsigned int distance_array[360]; // Only look forward
unsigned char Data_loop_index = 0;
boolean dataReady = false;


#include <Wire.h>

#include <Adafruit_NeoPixel.h>

#define NUMPIXELS  209
#define LED_PIN    6
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, LED_PIN, NEO_GRB + NEO_KHZ800);

int echoPin1 = 5;
int echoPin2 = 6;
int echoPin3 = 7;

unsigned long echoPin1riseTime;
unsigned long echoPin1fallTime;

unsigned long echoPin2riseTime;
unsigned long echoPin2fallTime;

unsigned long echoPin3riseTime;
unsigned long echoPin3fallTime;


// set this to the hardware serial port you wish to use
#define SerOut Serial1
#define SerLidar Serial2
#define SerIMU Serial3

//char[100] IMUBuffer;

void setup() {
  Wire.begin();
  Serial.begin(230400);
  SerOut.begin(115200);
  //SerOut.begin(230400);
  SerLidar.begin(115200);
  SerIMU.begin(115200);
  pixels.begin();
  /*
  attatchInterrupt(echoPin1, echoPin1high, RISING);
   attatchInterrupt(echoPin1, echoPin1low, FALLING);
   
   attatchInterrupt(echoPin2, echoPin2high, RISING);
   attatchInterrupt(echoPin2, echoPin2low, FALLING);
   
   attatchInterrupt(echoPin3, echoPin3high, RISING);
   attatchInterrupt(echoPin3, echoPin3low, FALLING);
   */
  for(int i=0; i<pixels.numPixels(); i++) {
    pixels.setPixelColor(i, pixels.Color(8, 0, 8));
  }
  pixels.show();
}


void loop() {
  int incomingByte;


  while (SerLidar.available() > 0) {
    char c = SerLidar.read();
    /*
    decodeData(c);
     //Serial.println(Data_loop_index);
     dataReady = true;
     if (dataReady) {
     dataReady = false;
     SerLidar.print("LIDAR");
     Serial.print(SerLidar.available());
     for (int i=90; i<270; i++) {
     //Serial.write(lowByte(distance_array[i]));
     //Serial.write(highByte(distance_array[i]));
     
     Serial.print(distance_array[i]);
     Serial.print(",");
     
     SerOut.print(distance_array[i]);
     SerOut.print(",");
     
     //SerLidar.write((unsigned char)(distance_array[i] & 0xFF));
     //SerLidar.write((unsigned char)((distance_array[i] >> 8) & 0xFF));
     }
     SerLidar.println();
     Serial.println();
     }*/
  }


  while (SerIMU.available() > 50) {
    //Serial.println(SerIMU.available());
    SerOut.print("IMU");
    //Serial.print("IMU");
    char c = SerIMU.read();
    while ( c != '\n') {
      SerOut.write(c);
      //
      Serial.write(c);
      c = SerIMU.read();
    }
    Serial.println();
    SerOut.println();
  }


  while (Serial.available() > 0) {
    char c = Serial.read(); // receive byte as a character

    if (c == 'w' && Serial.available() > 4) {
      byte n = Serial.read();
      byte r = Serial.read();
      byte g = Serial.read();
      byte b = Serial.read();
      pixels.setPixelColor(n, pixels.Color(r,g,b));
    } 
    else if (c == 'a') {
      for (int i=0; i<209; i++) {
        unsigned long itters = 0;
        while (Serial.available() < 3) if (itters++ > 1000000) break;
        byte r = Serial.read();
        byte g = Serial.read();
        byte b = Serial.read();
        pixels.setPixelColor(i, pixels.Color(r,g,b));
      }
      pixels.show();
    }
    else if (c == 'u') {
      pixels.show();
    }
  }



  while (SerOut.available() > 0) {
    char c = SerOut.read(); // receive byte as a character

    if (c == 'w' && SerOut.available() > 4) {
      byte n = SerOut.read();
      byte r = SerOut.read();
      byte g = SerOut.read();
      byte b = SerOut.read();
      pixels.setPixelColor(n, pixels.Color(r,g,b));
    } 
    else if (c == 'a') {
      for (int i=0; i<209; i++) {
        unsigned long itters = 0;
        while (SerOut.available() < 3) if (itters++ > 1000000) break;
        byte r = SerOut.read();
        byte g = SerOut.read();
        byte b = SerOut.read();
        pixels.setPixelColor(i, pixels.Color(r,g,b));
      }
      pixels.show();
    }
    else if (c == 'u') {
      pixels.show();
    }
  }

}












