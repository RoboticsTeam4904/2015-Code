unsigned long lastPrintout = 0;


uint16_t angle = 0;
unsigned int distance_array[360];
unsigned char flag1 = 0;
unsigned char flag2 = 0;
int inByte = 0; // incoming serial byte
unsigned char Data_status = 0;
unsigned char Data_4deg_index = 0;
unsigned char Data_loop_index = 0;
int dataIndex;


uint16_t data_status = 0;
uint16_t data_4deg_index = 0;
uint16_t data_loop_index = 0;
uint8_t motor_rph_high_byte = 0; 
uint8_t motor_rph_low_byte = 0;
uint8_t data0, data2;
uint16_t dist, quality;
uint16_t motor_rph = 0;



unsigned long curMillis;
unsigned long lastMillis = millis();
double pwm_last;
double motor_rpm;


void setup() {
  delay(500);
  Serial.begin(115200); // USB serial
  //Serial.println("Arduino Neato XV-11 Motor control board v0.1 by Cheng-Lung Lee, modified by Andrew");

  Serial1.begin(115200); // LIDAR serial
  //Serial.println("LIDAR connected");
  
  Serial2.begin(115200);
}

void loop() {
  // if we get a valid byte from LDS, read it and send it to USB-serial

  if ((millis()-lastPrintout) > 100) {
    lastPrintout += 100;
    Data_status = 0; 
    Data_4deg_index = 0; 
    Data_loop_index = 0;
  }
  
  if(Serial.available() > 0) {
    int requestedAngle = Serial.parseInt();
    Serial.println(distance_array[requestedAngle]);
    Serial.flush();
  }
  
  if(Serial2.available() > 0) {
    int requestedAngle = Serial2.parseInt();
    Serial.print(requestedAngle);
    Serial.print(" ");
    Serial2.println(distance_array[requestedAngle]);
    Serial.println(distance_array[requestedAngle]);
    Serial2.flush();
  }

  if(Serial1.available() > 0) {
    // get incoming byte:
    inByte = Serial1.read();
    //Serial.write(inByte);
    decodeData(inByte);
    angle %= 360;
  }
}


void decodeData(unsigned char inByte) {
  switch (data_status) {
  case 0: // no header
    if (inByte == 0xFA) {
      data_status = 1;
      data_loop_index = 1;
    }
    break;

  case 1: // find 2nd FA
    if (data_loop_index == 22) { // Theres 22 bytes in each packet. Time to start over
      if (inByte == 0xFA) {
        data_status = 2;
        data_loop_index = 1;
      } 
      else { // if not FA search again
        data_status = 0;
      }
    }
    else {
      data_loop_index++;
    }
    break;

  case 2: // read data out
    if (data_loop_index == 22) { // Theres 22 bytes in each packet. Time to start over
      if (inByte == 0xFA) {
        data_loop_index = 1;
      } 
      else { // if not FA search again
        data_status = 0;
      }
    }
    else {
      readData(inByte);
      data_loop_index++;
    }
    break;
  }

}
void readData(unsigned char inByte) {
  //Serial.write(inByte);
  switch (data_loop_index) {
  case 1: // 4 degree index
    data_4deg_index = inByte - 0xA0;
    if (data_4deg_index == 0) {
      angle = 0;

      curMillis = millis();
      lastMillis = curMillis;
    }
    //Serial.print(int(data_4deg_index));
    //Serial.println(F(" "));
    break;

  case 2: // speed in RPH low byte
    motor_rph_low_byte = inByte;
    break;

  case 3: // speed in RPH high byte
    motor_rph_high_byte = inByte;
    motor_rph = (motor_rph_high_byte << 8) | motor_rph_low_byte;
    motor_rpm = float( (motor_rph_high_byte << 8) | motor_rph_low_byte ) / 64.0;

    break;

  case 4:
    data0 = inByte; // first half of distance data
    break;

  case 5:
    if ((inByte & 0x80) >> 7) {  // check for Invalid Flag
      dist = 0;
    } 
    else {
      dist =  data0 | (( inByte & 0x3F) << 8);
    }
    break;

  case 6:
    data2 = inByte; // first half of quality data
    break;

  case 7:
    quality = (inByte << 8) | data2; 
    // actually store distance
    distance_array[angle] = dist;


    angle++;    
    break;

  case 8:
    data0 = inByte;
    break;

  case 9:
    if ((inByte & 0x80) >> 7) {  // check for Invalid Flag
      dist = 0;
    } 
    else {
      dist =  data0 | (( inByte & 0x3F) << 8);
    }
    break;

  case 10:
    data2 = inByte; // first half of quality data
    break;

  case 11:
    quality = (inByte << 8) | data2; 
    // actually store distance
    distance_array[angle] = dist;
    angle++;    
    break;

  case 12:
    data0 = inByte;
    break;

  case 13:
    if ((inByte & 0x80) >> 7) {  // check for Invalid Flag
      dist = 0;
    } 
    else {
      dist =  data0 | (( inByte & 0x3F) << 8);
    }
    break;

  case 14:
    data2 = inByte; // first half of quality data
    break;

  case 15:
    quality = (inByte << 8) | data2; 
    // actually store distance
    distance_array[angle] = dist;
    angle++;    
    break;

  case 16:
    data0 = inByte;
    break;

  case 17:
    if ((inByte & 0x80) >> 7) {  // check for Invalid Flag
      dist = 0;
    } 
    else {
      dist =  data0 | (( inByte & 0x3F) << 8);
    }
    break;

  case 18:
    data2 = inByte; // first half of quality data
    break;

  case 19:
    quality = (inByte << 8) | data2;
    // actually store distance
    distance_array[angle] = dist;
    angle++;    
    break;

  default: // others do checksum
    break;
  }  
}
