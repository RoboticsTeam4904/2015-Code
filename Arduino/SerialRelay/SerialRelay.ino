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
  Serial.begin(115200);
  SerOut.begin(115200);
  //SerOut.begin(230400);
  SerLidar.begin(115200);
  SerIMU.begin(115200);
  /*
  attatchInterrupt(echoPin1, echoPin1high, RISING);
   attatchInterrupt(echoPin1, echoPin1low, FALLING);
   
   attatchInterrupt(echoPin2, echoPin2high, RISING);
   attatchInterrupt(echoPin2, echoPin2low, FALLING);
   
   attatchInterrupt(echoPin3, echoPin3high, RISING);
   attatchInterrupt(echoPin3, echoPin3low, FALLING);
   */
}


void loop() {
  int incomingByte;


  while (SerLidar.available() > 0) {
    char c = SerLidar.read();
    //Serial.write(c);
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
    Serial.print("IMU");
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

}












