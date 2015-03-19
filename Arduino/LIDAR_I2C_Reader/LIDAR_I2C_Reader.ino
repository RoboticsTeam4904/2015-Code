#include <Wire.h>

int iter = 0;

void setup()
{
  Wire.begin();        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start serial for output
}

void loop()
{
  Wire.requestFrom(2, 6);    // request 6 bytes from slave device #2
  Wire.write(String(iter).c_str());
  
  String data = "";
  bool finished = false;
  while (Wire.available() && !finished) {
    int inChar = Wire.read();
    if(isDigit(inChar)) data += (char) inChar;
    if(inChar == '\n') finished = true;
  }
  String output = String(iter) + " " + data;
  Serial.println(output);

  delay(50);
  iter = (iter + 1) % 360;
}
