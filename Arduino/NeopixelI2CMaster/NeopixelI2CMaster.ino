// Wire Master Writer
// by Nicholas Zambetti <http://www.zambetti.com>

// Demonstrates use of the Wire library
// Writes data to an I2C/TWI slave device
// Refer to the "Wire Slave Receiver" example for use with this

// Created 29 March 2006

// This example code is in the public domain.


#include <Wire.h>

void setup() {
  Wire.begin(); // join i2c bus (address optional for master)
}

byte x = 0;

void loop() {
  for(int fadeValue = 0 ; fadeValue <= 255; fadeValue +=5) {
  setPixel(0, fadeValue, 0, 255-fadeValue);
  for (int i=1; i<32;i++) {
 setPixel(i, 255-fadeValue, 0, fadeValue); 
  }
  setPixel(32, fadeValue, 0, 255-fadeValue);
  updatePixels();
    delay(30);                            
  } 

  // fade out from max to min in increments of 5 points:
  for(int fadeValue = 255 ; fadeValue >= 0; fadeValue -=5) {
  setPixel(0, fadeValue, 0, 255-fadeValue); 
  for (int i=1; i<32;i++) {
 setPixel(i, 255-fadeValue, 0, fadeValue); 
  } 
  setPixel(32, fadeValue, 0, 255-fadeValue);
  updatePixels();
    delay(30);                            
  } 
  delay(500);
}

void setPixel(int num, byte r, byte g, byte b) {
  Wire.beginTransmission(4); // transmit to device #4
  Wire.write("w");        // sends five bytes
  Wire.write(num);              // sends one byte
  Wire.write(r);
  Wire.write(g);
  Wire.write(b);
  Wire.endTransmission();    // stop transmitting

}
void updatePixels() {
  Wire.beginTransmission(4);
  Wire.write("u");
  delay(1);
  Wire.endTransmission();
}

