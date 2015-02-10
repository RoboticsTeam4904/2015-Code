
#define I2C_SLAVE_BASE_ADDRESS 10 // the 7-bit address (remember to change this when adapting this example)
#define ENCODER_NUM 0
#include <TinyWireS.h>


#include "avr/interrupt.h"

long count = 0;
byte byteNum = 0;


void requestEvent() {
  byte lowByte = (byte) (count & 0xff);
  byte highByte = (byte) ((count >> 8) & 0xff);
  
  switch (byteNum) {
    case 1:
    TinyWireS.send(highByte);
    byteNum++;
    break;
    
    default:
    byteNum = 0;
    case 0:
    TinyWireS.send(lowByte);
    byteNum++;
    break;
  }
  
  //TinyWireS.send((byte)(count>>8));
  //TinyWireS.send((byte)(count>>16));
  //TinyWireS.send((byte)(count>>24));
}

/**
 * The I2C data received -handler
 *
 * This needs to complete before the next incoming transaction (start, data, restart/stop) on the bus does
 * so be quick, set flags for long running tasks to be called from the mainloop instead of running them directly,
 */
void receiveEvent(uint8_t howMany) {
}


void setup() {
  pinMode(3, INPUT); // OC1B-, Arduino pin 3, ADC
  pinMode(1, INPUT); // OC1A, also The only HW-PWM -pin supported by the tiny core analogWrite


  TinyWireS.begin(I2C_SLAVE_BASE_ADDRESS+ENCODER_NUM);
  TinyWireS.onReceive(receiveEvent);
  TinyWireS.onRequest(requestEvent);


  GIMSK = 0b00100000;    // turns on pin change interrupts
  PCMSK = 0b00010000;    // turn on interrupts on pins PB0, PB1, &amp; PB4
  sei();                 // enables interrupts
}

ISR(PCINT0_vect) {
  if (digitalRead(3) && digitalRead(4)) count++;
  else if(digitalRead(4)) count--;
}

void loop() {
  TinyWireS_stop_check();
}


