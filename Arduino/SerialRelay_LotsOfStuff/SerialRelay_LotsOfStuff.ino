/* UART Example, any character received on either the real
 serial port, or USB serial (or emulated serial to the
 Arduino Serial Monitor when using non-serial USB types)
 is printed as a message to both ports.
 
 This example code is in the public domain.
 */

#include <Wire.h>

// set this to the hardware serial port you wish to use
#define SerOut Serial1
#define SerLidar Serial2

void setup() {
  Wire.begin();
  Serial.begin(115200);
  SerOut.begin(115200);
  SerLidar.begin(115200);
  //pinMode(14, INPUT_PULLUP);
  //pinMode(15, INPUT_PULLUP);
}

void loop() {
  int incomingByte;

  while (SerLidar.available() > 0) {
    char c = SerLidar.read();
    SerOut.write(c);
    //Serial.write(c);
  }
  /*
  Serial.print(readEncoder(10));
  Serial.print("\t");
  Serial.print(readEncoder(11));
  Serial.print("\t");
  Serial.print(readEncoder(12));
  Serial.print("\t");
  Serial.print(readEncoder(13));
  Serial.print("\t");*/
  Serial.print(readEncoder(14));
  Serial.println();
  
  
}



long readEncoder(int i2cAddr) {
  
  Wire.beginTransmission(i2cAddr);
  Wire.write(0);
  delay(1); // somehow 
  Wire.endTransmission();
  Wire.requestFrom(i2cAddr, 4);    // request 6 bytes from slave device #2

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
