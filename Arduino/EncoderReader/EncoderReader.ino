// Wire Master Reader
// by Nicholas Zambetti <http://www.zambetti.com>

// Demonstrates use of the Wire library
// Reads data from an I2C/TWI slave device
// Refer to the "Wire Slave Sender" example for use with this

// Created 29 March 2006

// This example code is in the public domain.


#include <Wire.h>

void setup() {
  Wire.begin();        // join i2c bus (address optional for master)
  Serial.begin(115200);  // start serial for output
  while (!Serial);
  Serial.print("Running");
}

void loop() {
  Serial.print(readEncoder(10));
  Serial.print("\t");
  Serial.print(readEncoder(11));
  Serial.print("\t");
  Serial.print(readEncoder(12));
  Serial.print("\t");
  Serial.print(readEncoder(13));
  Serial.print("\t");
  Serial.print(readEncoder(14));
  Serial.println();
  

  delay(100);
}

long readEncoder(int i2cAddr) {
  
  Wire.beginTransmission(i2cAddr);
  Wire.write(0);
  delay(1); // somehow 
  Wire.endTransmission(true);
  Wire.requestFrom(i2cAddr, 4, true);    // request 6 bytes from slave device #2

  long dist = 0;
  byte counter = 0;
  while(Wire.available()) {   // slave may send less than requested
   
    byte c = Wire.read(); // receive a byte as character
    //Serial.print(c);         // print the character
    //Serial.print("\t");
    dist |=  c << (counter*8);
    counter++;
  }
  //Serial.print(dist);
  //Serial.println();
  return dist;
}
