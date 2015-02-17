
unsigned char Data_status = 0;



const byte EEPROM_ID = 0x04;  // used to validate EEPROM initialized

double pwm_val;
double pwm_last;
double motor_rpm;
unsigned long curMillis;
unsigned long lastMillis = millis();

uint8_t inByte = 0;  // incoming serial byte
uint16_t data_status = 0;
uint16_t data_4deg_index = 0;
uint16_t data_loop_index = 0;
uint8_t motor_rph_high_byte = 0; 
uint8_t motor_rph_low_byte = 0;
uint8_t data0, data2;
uint16_t dist, quality;
uint16_t motor_rph = 0;
uint16_t angle;

const int ledPin = 11;
boolean ledState = LOW;

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
  switch (data_loop_index) {
  case 1: // 4 degree index
    data_4deg_index = inByte - 0xA0;
    if (data_4deg_index == 0) {
      angle = 0;
      if (ledState) {
        ledState = LOW;
      } 
      else {
        ledState = HIGH;
      }
      digitalWrite(ledPin, ledState);
      if (true) {
        curMillis = millis();
        Serial.print(F("Time Interval: "));
        Serial.println(curMillis - lastMillis);
        lastMillis = curMillis;
      }
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
    if (true) {
      Serial.print(F("RPM: "));
      Serial.print(motor_rpm);
      Serial.print(F("  PWM: "));   
      Serial.println(pwm_val);
    }
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
    if (true) {
      if (true) {
        Serial.print(angle);
        Serial.print(F(": "));
        Serial.print(int(dist));
        Serial.print(F(" ("));
        Serial.print(quality);
        Serial.println(F(")"));
      }
    }
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
    if (true) {
      if (true) {
        Serial.print(angle);
        Serial.print(F(": "));
        Serial.print(int(dist));
        Serial.print(F(" ("));
        Serial.print(quality);
        Serial.println(F(")"));
      }
    }
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
    if (true) {
      if (true) {
        Serial.print(angle);
        Serial.print(F(": "));
        Serial.print(int(dist));
        Serial.print(F(" ("));
        Serial.print(quality);
        Serial.println(F(")"));
      }
    }
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
    if (true) {
      if (true) {
        Serial.print(angle);
        Serial.print(F(": "));
        Serial.print(int(dist));
        Serial.print(F(" ("));
        Serial.print(quality);
        Serial.println(F(")"));
      }
    }
    angle++;    
    break;

  default: // others do checksum
    break;
  }  
}
