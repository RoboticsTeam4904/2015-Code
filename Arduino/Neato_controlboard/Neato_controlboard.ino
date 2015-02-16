// Global variables for LIDAR
unsigned int distance_array[180]; // Only look forward
unsigned char Data_loop_index = 0;

void setup() {
  Serial2.begin(115200);  // XV-11 LDS data

}

void loop() {
  // if we get a valid byte from LDS, read it and send it to USB-serial
  if (Serial2.available() > 0) {
    decodeData(Serial2.read());
  }

}

void decodeData(unsigned char inByte) {

  unsigned char Data_status = 0;

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
        } else // if not FA search again
          Data_status = 0;
      } else {
        Data_loop_index++;
      }
      break;

    case 2: // Read data out6
      if (Data_loop_index == 22) {
        if (inByte == 0xFA) {
          Data_loop_index = 1;
        }
        else // if not FA search again
          Data_status = 0;
      } else {
        readData(inByte);
        Data_loop_index++;
      }
      break;
  }

}

void readData(unsigned char inByte) {
  unsigned char distanceLow = 0;
  unsigned char distanceHigh = 0;
  unsigned char flag1 = 0;
  unsigned char flag2 = 0;
  double angle = 0;
  int r = 115;
  int rMin = 15;
  unsigned char Data_4deg_index = 0;
  unsigned int distance;

  switch (Data_loop_index) {

    case 1: // 4 degree index
      Data_4deg_index = inByte - 0xA0;
      break;
    case 2: // Speed in RPH low byte
      // I don't care!
      break;
    case 3: // Speed in RPH high byte
      // I don't care!
      break;
    case 4:
    case 8:
    case 12:
    case 16:
      distanceLow = inByte;
      break;
    case 5:
    case 9:
    case 13:
    case 17:
      distanceHigh = inByte & B00111111; //mask 14 and 15 bits
      flag1 = inByte & B10000000;
      flag2 = inByte & B01000000;
      distance = (distanceHigh << 8) | distanceLow;
      if (true) {
        int dataIndex;
        switch (Data_loop_index) {
          case 5:
            dataIndex = 0;
            break;
          case 9:
            dataIndex = 1;
            break;
          case 13:
            dataIndex = 2;
            break;
          case 17:
            dataIndex = 3;
            break;
        }
        int angleIndex = Data_4deg_index * 4 + dataIndex;
        if (angleIndex < 90 || angleIndex > 270) {
          if(angleIndex > 270){
            angleIndex = 90;
          }
          if (flag1 == 0 && flag2 == 0) {
            distance_array[angleIndex] = distance;
          } else {
            distance_array[angleIndex] = 0;
          }
        }
      }
      break;
    default: // others do checksum
      break;
  }
}
