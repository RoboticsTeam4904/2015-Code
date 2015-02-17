/* UART Example, any character received on either the real
 serial port, or USB serial (or emulated serial to the
 Arduino Serial Monitor when using non-serial USB types)
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


// set this to the hardware serial port you wish to use
#define SerOut Serial1
#define SerLidar Serial2
#define SerIMU Serial3

//char[100] IMUBuffer;

void setup() {
  Wire.begin();
  Serial.begin(230400);
  //SerOut.begin(921600);
  SerOut.begin(230400);
  SerLidar.begin(115200);
  SerIMU.begin(115200);
  pixels.begin();
  pixels.show();

}

void loop() {
  int incomingByte;


  while (SerLidar.available() > 0) {
    char c = SerLidar.read();
    
    decodeData(c);/*
    //Serial.println(Data_loop_index);
    if (dataReady) {
      dataReady = false;
      for (int i=0; i<10; i++) {
        Serial.print(distance_array[i]);
        Serial.print(" ");
      }
      Serial.println();
    }*/
  }

  /*
    SerOut.print("LIDAR");
   //Serial.print("LIDAR");
   for (int i=0; i<50; i++) {
   char c = SerLidar.read();
   SerOut.write(c);
   //Serial.write(c);
   }
   SerOut.println();
   //Serial.println();*/



  while (SerIMU.available() > 50) {
    //Serial.println(SerIMU.available());
    SerOut.print("IMU");
    //Serial.print("IMU");
    char c = SerIMU.read();
    while ( c != '\n') {    //int i=0; i<45; i++) {
      SerOut.write(c);
      //Serial.write(c);
      c = SerIMU.read();
    }
    //Serial.println();
    SerOut.println();
  }


  while (Serial.available() > 0) {
    char c = Serial.read(); // receive byte as a character

    if (c == 'w' && Serial.available() > 4) {
      byte o = Serial.read();
      byte r = Serial.read();
      byte g = Serial.read();
      byte b = Serial.read();
      pixels.setPixelColor(o, pixels.Color(r,g,b));
    } else if (c == 'u') {
      pixels.show();
    }
  }

}




