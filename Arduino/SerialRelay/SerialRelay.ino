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
#define SerIMU Serial3

//char[100] IMUBuffer;

void setup() {
  Wire.begin();
  Serial.begin(230400);
  //SerOut.begin(921600);
  SerOut.begin(230400);
  SerLidar.begin(115200);
  SerIMU.begin(115200);

}

void loop() {
  int incomingByte;


  while (SerLidar.available() > 50) {
    SerOut.print("LIDAR");
    //Serial.print("LIDAR");
    for (int i=0; i<50; i++) {
      char c = SerLidar.read();
      SerOut.write(c);
      //Serial.write(c);
    }
    SerOut.println();
    //Serial.println();
  }
  /*
  while (SerIMU.available()) {
   char c = SerIMU.read();
   SerOut.write(c);
   Serial.write(c);
   }
   */
  while (SerIMU.available() > 50) {
    Serial.println(SerIMU.available());
    SerOut.print("IMU");
    //Serial.print("IMU");
    char c = SerIMU.read();
    while ( c != '\n') {    //int i=0; i<45; i++) {
      SerOut.write(c);
      Serial.write(c);
      c = SerIMU.read();
    }
    Serial.println();
    SerOut.println();
  }


  while (Serial.available() > 0) {
    char c = Serial.read();
    SerOut.write(c);
    Serial.write(c);
  }


}





