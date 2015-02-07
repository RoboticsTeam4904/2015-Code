
#define I2C_SLAVE_BASE_ADDRESS 0x4 // the 7-bit address (remember to change this when adapting this example)
#define ENCODER_NUM 0
#include <TinyWireS.h>


#include "avr/interrupt.h"

byte count = 0;



void requestEvent() {  
  TinyWireS.send(count);
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


  TinyWireS.begin(I2C_SLAVE_ADDRESS+ENCODER_NUM);
  TinyWireS.onReceive(receiveEvent);
  TinyWireS.onRequest(requestEvent);


  GIMSK = 0b00100000;    // turns on pin change interrupts
  PCMSK = 0b00010000;    // turn on interrupts on pins PB0, PB1, &amp; PB4
  sei();                 // enables interrupts
}

ISR(PCINT0_vect) {
  if (digitalRead(3)) count++;
  else count--;
}

void loop() {
  TinyWireS_stop_check();
}


