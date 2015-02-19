double angle = 0;
unsigned int distance_array[360];
unsigned char flag1 = 0;
unsigned char flag2 = 0;
int inByte = 0; // incoming serial byte
unsigned char Data_status = 0;
unsigned char Data_4deg_index = 0;
unsigned char Data_loop_index = 0;
int dataIndex;


void setup() {
  delay(500);
  Serial.begin(115200); // USB serial
  Serial.println("Arduino Neato XV-11 Motor control board v0.1 by Cheng-Lung Lee, modified by Andrew");
}

void loop() {
  // if we get a valid byte from LDS, read it and send it to USB-serial
  if (Serial.available() > 0) {
    // get incoming byte:
    inByte = Serial.read();
    //Serial.write(inByte);
    decodeData(inByte);

    if (dataIndex == 3 && Data_4deg_index == 89) {
      Data_status = 0; Data_4deg_index = 0; Data_loop_index = 0;

      for (int i = 0; i < 360; i++) {
        if (distance_array[i] != 0) {
          Serial.print(i);
          Serial.print(":");
          Serial.print(distance_array[i]);
          Serial.print(" ");
        }
      }
      Serial.println();
      delay(250);
    }
  }
}
void decodeData(unsigned char inByte) {
  switch (Data_status) {
    case 0: // no header
      if (inByte == 0xFA) {
        Data_status = 1;
        Data_loop_index = 1;
      }
      break;
    case 1: // Find 2nd FA
      if (Data_loop_index == 22) {
        if (inByte == 0xFA) {
          Data_status = 2;
          Data_loop_index = 1;
        }
        else // if not FA search again
          Data_status = 0;
      }
      else {
        Data_loop_index++;
      }
      break;
    case 2: // Read data out
      if (Data_loop_index == 22) {
        if (inByte == 0xFA) {
          Data_loop_index = 1;
        }
        else // if not FA search again
          Data_status = 0;
      }
      else {
        readData(inByte);
        Data_loop_index++;
      }
      break;
  }
}
void readData(unsigned char inByte) {
  unsigned char distanceSnips [4] [2];
  switch (Data_loop_index) {
    case 1: // 4 degree index
      Data_4deg_index = inByte - 0xA0;
      break;
    case 2: // Speed in RPH low byte
      // #dontcare
      break;
    case 3: // Speed in RPH high byte
      // #dontcare
      break;
    case 4:
      distanceSnips[0][0] = inByte;
      break;
    case 5:
      dataIndex = 0;
      distanceSnips[dataIndex][1] = inByte & B00111111; //mask 14 and 15 bits
      parseDistance(distanceSnips[dataIndex][1], distanceSnips[dataIndex][0]);
      break;
    case 8:
      distanceSnips[1][0] = inByte;
      break;
    case 9:
      dataIndex = 1;
      distanceSnips[dataIndex][1] = inByte & B00111111; //mask 14 and 15 bits
      parseDistance(distanceSnips[dataIndex][1], distanceSnips[dataIndex][0]);
      break;
    case 12:
      distanceSnips[2][0] = inByte;
      break;
    case 13:
      dataIndex = 2;
      distanceSnips[dataIndex][1] = inByte & B00111111; //mask 14 and 15 bits
      parseDistance(distanceSnips[dataIndex][1], distanceSnips[dataIndex][0]);
      break;
    case 16:
      distanceSnips[3][0] = inByte;
    case 17:
      dataIndex = 3;
      distanceSnips[dataIndex][1] = inByte & B00111111; //mask 14 and 15 bits
      parseDistance(distanceSnips[dataIndex][1], distanceSnips[dataIndex][0]);
      break;
  }
}
int parseDistance(unsigned char distanceHigh, unsigned char distanceLow) {
  flag1 = inByte & B10000000;
  flag2 = inByte & B01000000;
  int distance = (distanceHigh << 8) | distanceLow;
  int angleIndex = Data_4deg_index * 4 + dataIndex;
  if (flag1 == 0 && flag2 == 0) { // invalid data and strength warning flags
    distance_array[angleIndex] = distance;
  }
  else {
    distance_array[angleIndex] = 0;
  }
}

