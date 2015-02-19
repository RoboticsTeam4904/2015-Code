double angle=0;
int r=115;
int rMin=15;
unsigned int distance;
unsigned int distance_array[360];
unsigned char distanceLow=0;
unsigned char distanceHigh=0;
unsigned char flag1=0;
unsigned char flag2=0;
int inByte = 0; // incoming serial byte
unsigned char Data_status=0;
unsigned char Data_4deg_index=0;
unsigned char Data_loop_index=0;

void setup() {
  delay(500);
  Serial.begin(115200); // USB serial
  Serial.println("Arduino Neato XV-11 Motor control board v0.1 by Cheng-Lung Lee");
}

void loop() {
  // if we get a valid byte from LDS, read it and send it to USB-serial
  if (Serial.available() > 0) {
    // get incoming byte:
    inByte = Serial.read();
    //Serial.write(inByte);
    decodeData(inByte);
  }
}
void decodeData(unsigned char inByte){
  switch (Data_status){
  case 0: // no header
    if (inByte==0xFA){
      Data_status=1;
      Data_loop_index=1;
    }
    break;
  case 1: // Find 2nd FA
    if (Data_loop_index==22){
      if (inByte==0xFA){
        Data_status=2;
        Data_loop_index=1;
      }
      else // if not FA search again
      Data_status=0;
    }
    else{
      Data_loop_index++;
    }
    break;
  case 2: // Read data out
    if (Data_loop_index==22){
      if (inByte==0xFA){
        Data_loop_index=1;
      }
      else // if not FA search again
      Data_status=0;
    }
    else{
      readData(inByte);
      Data_loop_index++;
    }
    break;
  }
}
void readData(unsigned char inByte){
  switch (Data_loop_index){
  case 1: // 4 degree index
    Data_4deg_index=inByte-0xA0;
    break;
  case 2: // Speed in RPH low byte
    // #dontcare
    break;
  case 3: // Speed in RPH high byte
    // #dontcare
    break;
  case 4:
  case 5:
  case 8:
  case 9:
  case 12:
  case 13:
  case 16:
    distanceLow=inByte;
  case 17:
    distanceHigh=inByte & B00111111; //mask 14 and 15 bits
    flag1=inByte & B10000000;
    flag2=inByte & B01000000;
    distance=(distanceHigh<<8)|distanceLow;
    if (true){
      int dataIndex;
      switch (Data_loop_index){
      case 5:
        dataIndex=0;
        break;
      case 9:
        dataIndex=1;
        break;
      case 13:
        dataIndex=2;
        break;
      case 17:
        dataIndex=3;
        break;
      }
      int angleIndex=Data_4deg_index*4 + dataIndex;
      if (flag1==0 && flag2==0){
        distance_array[angleIndex]=distance;
      }
      else{
        distance_array[angleIndex]=0;
      }
      if (dataIndex==3 && Data_4deg_index==89){
        Data_status = 0; Data_4deg_index = 0; Data_loop_index = 0; angleIndex = 0;
        for(int i = 0; i < 360; i++){
          if(distance_array[i] != 0){
            Serial.print(i);
            Serial.print(" ");
            Serial.println(distance_array[i]);
          }
        }
        
      }
    }
    break;
  default: // others do checksum
    break;
  }
}

