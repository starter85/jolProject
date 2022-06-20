#pragma once // 헤더파일을 한 번만 include
#define nDelay 5000

class WaterPump // WaterPump라는 class 정의
{
public: // 외부에서 접근 가능한 함수
	// 생성자(constructor): 출력 생략: 생성될 때 호출되는 함수; 초기화; memory 할당
	WaterPump(void)
	{
	}
	// 파괴자(desturctor): 입출력 생략; 파괴될 때 호출되는 함수; memory 해제
	~WaterPump()
	{
	}

	void init(void)
	{
		pinMode(AA, OUTPUT);  // AA를 출력 핀으로 설정
		pinMode(AB, OUTPUT);  // AB를 출력 핀으로 설정
		pinMode(BA, OUTPUT);  // BA를 출력 핀으로 설정
		pinMode(BB, OUTPUT);  // BB를 출력 핀으로 설정
	}

	void waterPumpOn()
	{
		init();
		Serial.println("water pump ON");
		// 초코칩통 빨아들여서 내뱉기, 물 끓이는곳
		digitalWrite(AA, HIGH);
		digitalWrite(AB, LOW);
		delay(2000);
  		//커피컵 빨아들여서 내뱉기 ,면 삶는곳, 순환시작
		digitalWrite(BA, HIGH);
		digitalWrite(BB, LOW);
		
	}

	void waterPumpStop() {
		// 초코칩통 빨아들여서 내뱉기, 물 끓이는곳 공급 중지
		digitalWrite(AA, LOW);
		digitalWrite(AB, LOW);
		delay(5000); //물 빼는 시간
		digitalWrite(BA, LOW);
		digitalWrite(BB, LOW);
		Serial.println("water pump OFF");
	}

protected: // 외부에서 보호되는 변수/함수
	// Property(성질): class의 변수
	int AA = 4; // AA를 연결한 디지털 핀
	int AB = 5;  // AB를 연결한 디지털 핀
	int BA = 2;  // BA를 연결한 디지털 핀
	int BB = 3;  // BB를 연결한 디지털 핀
};