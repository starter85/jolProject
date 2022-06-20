#include <Servo.h> // Servo 함수 호출
Servo myservo1;
Servo myservo2;
Servo myservo3;
Servo servo_noodle; 
Servo servo_source;

#define PIN_SERVO1 (23) // 마늘
#define PIN_SERVO2 (24) // 페퍼론치노
#define PIN_SERVO3 (25) // 파마산가루  
#define PIN_SERVO_n (26) // 면
#define PIN_SERVO_s (44) //소스
char s_moter_on;
int pos; 

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial1.begin(9600);
  
  // servo init
  myservo1.attach(PIN_SERVO1); // 마늘
  myservo2.attach(PIN_SERVO2); // 페퍼론치노
  myservo3.attach(PIN_SERVO3); // 파마산가루  
  servo_noodle.attach(PIN_SERVO_n);
  servo_source.attach(PIN_SERVO_s);
  myservo1.write(0);
  myservo2.write(0);
  myservo3.write(0);
  servo_noodle.write(0);
  servo_source.write(0);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial1.available()){
    s_moter_on = (char)Serial1.read();

    // 재료1칸(마늘) 서보모터 작동
    if(s_moter_on=='1'){
      Serial.println("drop garlic");
      for (pos = 0; pos <= 90; pos += 3) {
        myservo1.write(pos);
        delay(15);
      }
      delay(1000);
      for (pos = 90; pos >= 0; pos -= 3) {
        myservo1.write(pos);
        delay(15);
      }
    }
    
    //소스칸 서보모터 작동
    else if(s_moter_on=='s'){
      Serial.println("drop source");
      for (pos = 0; pos <= 90; pos += 3) {
        servo_source.write(pos);
        delay(15);
      }
      delay(1000);
      for (pos = 90; pos >= 0; pos -= 3) {
        servo_source.write(pos);
        delay(15);
      }
    }
      
      
     //면 칸 서보모터 작동
     else if(s_moter_on=='n'){
      Serial.println("drop noodle");
      for (pos = 0; pos <= 90; pos += 3) {
        servo_noodle.write(pos);
        delay(15);
      }
      delay(1000);
      for (pos = 90; pos >= 0; pos -= 3) {
        servo_noodle.write(pos);
        delay(15);
      }
     }

     //두번째 재료칸(페퍼론치노) 서보모터 작동
     else if(s_moter_on=='2'){
      Serial.println("drop pepperoncino");
      for (pos = 0; pos <= 90; pos += 3) {
        myservo2.write(pos);
        delay(15);
      }
      delay(1000);
      for (pos = 90; pos >= 0; pos -= 3) {
        myservo2.write(pos);
        delay(15);
      }
     }

     
      //세번째 재료칸(파마산가루) 서보모터 작동
      else if(s_moter_on=='3'){
      Serial.println("drop Parmesan");
      for (pos = 0; pos <= 90; pos += 3) {
        myservo3.write(pos);
        delay(15);
      }
      delay(1000);
      for (pos = 90; pos >= 0; pos -= 3) {
        myservo3.write(pos);
        delay(15);
      }
     }
  }
}
