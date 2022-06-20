# pragma once // 헤더파일을 한번만 include

class DCMotor // DCMotor라는 class 정의
{
public: // 외부에서 접근 가능한 함수
	// 생성자(constructor): 출력 생략: 생성될 때 호출되는 함수; 초기화; memory 할당
	DCMotor(void){}
	// 파괴자(destructor): 입출력 생략; 파괴될 때 호출되는 함수; memory 해제
	~DCMotor(){}
	
	// 초기화
	void init(void)
	{
		pinMode(IN1Pin, OUTPUT);
		pinMode(IN2Pin, OUTPUT);
		analogWrite(ENPin, 5); // 0-255 motor speed.
		Serial.println("dcmotor init");
    }
	// 요리시작
	void cook(void)
	{
		init();
		digitalWrite(IN1Pin, HIGH);
		digitalWrite(IN2Pin, LOW);
		Serial.println("dcmotor on, start cooking");
	}
	// 요리중지
	void stopCook(){
		digitalWrite(IN1Pin, LOW);
		digitalWrite(IN2Pin, LOW);
		Serial.println("dcmotor stop");
	}

protected: // 외부에서 보호되는 변수/함수
	// Property(성질): class의 변수
	int IN1Pin = 44; //  Pin 번호: 44
	int IN2Pin = 45; //  Pin 번호: 45
	int ENPin = 46;
};