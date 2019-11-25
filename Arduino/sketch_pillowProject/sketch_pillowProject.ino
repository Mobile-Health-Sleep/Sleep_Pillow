#define LIGHTWEIGHT 1

#include <SPI.h>
#include "Adafruit_BLE_UART.h"
#include <aREST.h>

aREST rest = aREST();

Adafruit_BLE_UART BTLEserial = Adafruit_BLE_UART(10, 2, 9);

int accelX, accelY, accelZ;
int temp = 0;
int light = 0;
boolean vibrate = false;

void setup() {
  BTLEserial.begin();

  rest.variable("temp", &temp);
  rest.variable("light", &light);
  rest.variable("accelX", &accelX);
  rest.variable("accelY", &accelY);
  rest.variable("accelZ", &accelZ);

  rest.set_id("001");
  rest.set_name("Pillow");
  pinMode(5, OUTPUT);

  BTLEserial.setDeviceName("Pillow");
}

aci_evt_opcode_t laststatus = ACI_EVT_DISCONNECTED;

void loop() {
  // put your main code here, to run repeatedly:
  BTLEserial.pollACI();

  aci_evt_opcode_t status = BTLEserial.getState();

  light = analogRead(A5);
  temp = analogRead(A4);

  if(vibrate) {
    vibration();
  }

  if(status != laststatus){
    laststatus = status;
  }
  
  // Handle REST calls
  if(status == ACI_EVT_CONNECTED){
    rest.handle(BTLEserial);
  }
  delay(100);
}


void vibration() {
  digitalWrite(5, HIGH);
  delay(2000);
  digitalWrite(5, LOW);
}
