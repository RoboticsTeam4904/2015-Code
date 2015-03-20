
#define I2C_SLAVE_BASE_ADDRESS 10 // the 7-bit address (remember to change this when adapting this example)
#define ENCODER_NUM 4
#include <Wire.h>


#include "avr/interrupt.h"

long count = 0;
byte byteNum = 0;


void requestEvent() {
  Wire.write(count);/*
  byte lowByte = (byte) (count & 0xff);
  byte mid1Byte = (byte) ((count >> 8) & 0xff);
  byte mid2Byte = (byte) ((count >> 16) & 0xff);
  byte highByte = (byte) ((count >> 24) & 0xff);
  
  switch (byteNum) {
    case 1:
    Wire.write(mid1Byte);
    byteNum++;
    break;
    
    case 2:
    Wire.write(mid2Byte);
    byteNum++;
    break;
    
    case 3:
    Wire.write(highByte);
    byteNum++;
    break;
    
    default:
    byteNum = 0;
    case 0:
    Wire.write(lowByte);
    byteNum++;
    break;
  }
  
  //TinyWireS.send((byte)(count>>8));
  //TinyWireS.send((byte)(count>>16));
  //TinyWireS.send((byte)(count>>24));*/
}

/**
 * The I2C data received -handler
 *
 * This needs to complete before the next incoming transaction (start, data, restart/stop) on the bus does
 * so be quick, set flags for long running tasks to be called from the mainloop instead of running them directly,
 */
void receiveEvent(uint8_t howMany) {
  byteNum = Wire.read();
}


void setup() {
  pinMode(3, INPUT); // OC1B-, Arduino pin 3, ADC
  pinMode(2, INPUT); // OC1A, also The only HW-PWM -pin supported by the tiny core analogWrite


  Wire.begin(I2C_SLAVE_BASE_ADDRESS+ENCODER_NUM);
  //Wire.onReceive(receiveEvent);
  Wire.onRequest(requestEvent);
  
  attachInterrupt(0, pinchange, RISING);
}

void pinchange() {
  if (digitalRead(2)) count++;
  else count--;
}

void loop() {
  delay(100);
  Serial.println(count);
}


