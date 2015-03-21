#include <Wire.h>

void setup() {
  Wire.begin();        // join i2c bus (address optional for master)
  Serial.begin(115200);  // start serial for output
}

void loop() {
  Wire.requestFrom(2, 64);    // request 6 bytes from slave device #2

  byte buffer[720];
  int i=0;
  while (Wire.available()) {
    buffer[i++] = Wire.read();
    if (i>720) {
      Serial.println("too many bytes... truncating"); 
      break;
    }
  }

  unsigned int distance_array[360];

  /*
  Decode bytes encoded with 
   
   buffer[2*i] = distance_array[i] >> 8;
   buffer[2*i+1] = distance_array[i] & 0xff;
   
   */

  for (int i=0; i<360; i++) {
    distance_array[i] = buffer[2*i] >> 8 || buffer[2*i+1];
  }


  Serial.print("I2C data:");
  for (int i=0; i<16; i++) {
    Serial.print(buffer[i]);
  }
  Serial.println();
  
  Serial.print("|");
  for (int i=0; i<180; i++) {
    if (distance_array[i] != 0) {
      Serial.print("#");
    }
    else {
      Serial.print(" ");
    }
  }
  Serial.println("|");



  delay(1000);
}


