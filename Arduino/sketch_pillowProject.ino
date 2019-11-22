#define LIGHTWEIGHT 1

#include <SPI.h>
#include "Adafruit_BLE_UART.h"
#include <aREST.h>

#define ADAFRUITBLE_REQ 10
#define ADAFRUITBLE_RDY 2
#define ADAFRUITBLE_RST 9

aREST rest = aREST();

Adafruit_BLE_UART BTLEserial = Adafruit_BLE_UART(ADAFRUITBLE_REQ, ADAFRUITBLE_RDY, ADAFRUITBLE_RST);

const int MPU_addr=0x68;

int accelX, accelY, accelZ;
int temp = 0;
int light = 0;
boolean vibrate = false;

void setup() {
  Serial.begin(9600);
  BTLEserial.begin();

  rest.variable("temp", &temp);
  rest.variable("light", &light);

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

  /*
  Serial.print("Temp = ");
  Serial.println(temp);
  Serial.print("Light = ");
  Serial.println(light);

  Serial.println();
  Serial.println();
  Serial.println();
  */
  
  if(status != laststatus){
    //print it out
    if(status == ACI_EVT_DEVICE_STARTED){
      Serial.println(F("* Advertising started"));
    }
    
    if(status == ACI_EVT_CONNECTED){
      Serial.println(F("* Connected!"));
    }
    
    if(status == ACI_EVT_DISCONNECTED){
      Serial.println(F("* Disconnected or advertising timed out"));
    }
    
    // Set the last status
    laststatus = status;
  }
  
  // Handle REST calls
  if(status == ACI_EVT_CONNECTED){
    if(BTLEserial.available()) {
      Serial.print("* ");
      Serial.print(BTLEserial.available());
      Serial.println(F(" bytes available from BTLE"));
    }

    while(BTLEserial.available()) {
      char c = BTLEserial.read();
      Serial.print(c);
    }
    
    rest.handle(BTLEserial);
  }
  delay(300);
}


void vibration() {
  digitalWrite(5, HIGH);
  delay(2000);
  digitalWrite(5, LOW);
}
