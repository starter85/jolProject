#pragma once
#define PIN_HEAT_1 (8) //모스펫 1, 면 끓이는 패드
#define PIN_HEAT_2 (9) //모스펫 2, 요리 하는 패드
#define Temperature (A0)
#define PIN_HEAT_LED (7)
#define WaterBoilingTime (5000) // 물 끓는데 걸리는 시간 측정해서 값을 바꿔주기
#define CookBoilingTime (5000) // 요리하는 곳 온도 올리는데 걸리는 시간 측정해서 값을 바꿔주기

class HEAT_PAD
{
public:
	HEAT_PAD(void)
	{
		
	}
	~HEAT_PAD()
	{}

	void heatPad_init(void)
	{
		pinMode(PIN_HEAT_1, OUTPUT);
		pinMode(PIN_HEAT_2, OUTPUT);
		pinMode(PIN_HEAT_LED, OUTPUT);
	}

	void boillingWater(void) {
		Serial.println("water heat pad on");
		digitalWrite(PIN_HEAT_LED, HIGH); // 확인하는 LED
		digitalWrite(PIN_HEAT_1, HIGH); // 물 끓이기
		while (true) {
			if (temperature>23) {	// 임시로 5초로 설정해놨음
				break;
			}
			else {
				value = analogRead(Temperature);
				voltage = value * 5.0 / 1024;     // 온도센서의 값을 전압으로 변환
				temperature = (voltage - 0.5) * 100; //섭씨온도로 변환
				Serial.print(temperature);
				Serial.println(" C");
				delay(500);
			}
		}
	}

	void boillingCook(void) {
		Serial.println("cook heat pad on");
		digitalWrite(PIN_HEAT_LED, HIGH); // 확인하는 LED
		digitalWrite(PIN_HEAT_2, HIGH); // 물 끓이기
		boiling_start = millis(); // ms 기준
		while (true) {
			boiling_end = millis(); // ms 기준
			if (boiling_end - boiling_start > CookBoilingTime) {	// 임시로 5초로 설정해놨음
				boiling_end = 0;
				break;
			}
			else {
				value = analogRead(Temperature);
				voltage = value * 5.0 / 1024;     // 온도센서의 값을 전압으로 변환
				temperature = (voltage - 0.5) * 100; //섭씨온도로 변환
				Serial.print(temperature);
				Serial.println(" C");
				delay(500);
			}
		}
	}

	void WaterHeatPad_On(void)
	{
		heatPad_init();
		boillingWater();
	}

	void WaterHeatPad_Off(void)
	{
		digitalWrite(PIN_HEAT_LED, LOW);
		digitalWrite(PIN_HEAT_1, LOW); // 물 끓이기 OFF
		Serial.println("water heat pad off");
	}

	void CookHeatPad_On(void)
	{
		boillingCook();
	}

	void CookHeatPad_Off(void)
	{
		digitalWrite(PIN_HEAT_LED, LOW);
		digitalWrite(PIN_HEAT_2, LOW); // 요리하기 OFF
		Serial.println("cook heat pad off");
	}

protected: // 외부에서 보호되는 변수/함수
// Property(성질): class의 변수
	int value = 0;
	float voltage = 0.0;
	float temperature = 0.0;
	unsigned long boiling_start = 0;
	unsigned long boiling_end = 0;
};