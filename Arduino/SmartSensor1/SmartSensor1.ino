#include <SmartSensor1.h>

#define SERIAL_BPS (9600) // 통신 속도: 9600
#define SERIAL1_BPS (9600) // 통신 속도: 9600

void setup(){
  Serial.begin(SERIAL_BPS);
  Serial1.begin(SERIAL1_BPS);
} 


void loop() {
  // put your main code here, to run repeatedly:
  SmartSensor1 smartSensor1; // SmartSensor1: class type, smartSensor1: class type으로 만든 변수
  //smartSensor1.init(); // 함수 초기화
  smartSensor1.BTN_run(); // 함수 시작
}
