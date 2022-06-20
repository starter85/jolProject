#pragma once
#define PIN_HEAT_1 (8) //���� 1, �� ���̴� �е�
#define PIN_HEAT_2 (9) //���� 2, �丮 �ϴ� �е�
#define Temperature (A0)
#define PIN_HEAT_LED (7)
#define WaterBoilingTime (5000) // �� ���µ� �ɸ��� �ð� �����ؼ� ���� �ٲ��ֱ�
#define CookBoilingTime (5000) // �丮�ϴ� �� �µ� �ø��µ� �ɸ��� �ð� �����ؼ� ���� �ٲ��ֱ�

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
		digitalWrite(PIN_HEAT_LED, HIGH); // Ȯ���ϴ� LED
		digitalWrite(PIN_HEAT_1, HIGH); // �� ���̱�
		while (true) {
			if (temperature>23) {	// �ӽ÷� 5�ʷ� �����س���
				break;
			}
			else {
				value = analogRead(Temperature);
				voltage = value * 5.0 / 1024;     // �µ������� ���� �������� ��ȯ
				temperature = (voltage - 0.5) * 100; //�����µ��� ��ȯ
				Serial.print(temperature);
				Serial.println(" C");
				delay(500);
			}
		}
	}

	void boillingCook(void) {
		Serial.println("cook heat pad on");
		digitalWrite(PIN_HEAT_LED, HIGH); // Ȯ���ϴ� LED
		digitalWrite(PIN_HEAT_2, HIGH); // �� ���̱�
		boiling_start = millis(); // ms ����
		while (true) {
			boiling_end = millis(); // ms ����
			if (boiling_end - boiling_start > CookBoilingTime) {	// �ӽ÷� 5�ʷ� �����س���
				boiling_end = 0;
				break;
			}
			else {
				value = analogRead(Temperature);
				voltage = value * 5.0 / 1024;     // �µ������� ���� �������� ��ȯ
				temperature = (voltage - 0.5) * 100; //�����µ��� ��ȯ
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
		digitalWrite(PIN_HEAT_1, LOW); // �� ���̱� OFF
		Serial.println("water heat pad off");
	}

	void CookHeatPad_On(void)
	{
		boillingCook();
	}

	void CookHeatPad_Off(void)
	{
		digitalWrite(PIN_HEAT_LED, LOW);
		digitalWrite(PIN_HEAT_2, LOW); // �丮�ϱ� OFF
		Serial.println("cook heat pad off");
	}

protected: // �ܺο��� ��ȣ�Ǵ� ����/�Լ�
// Property(����): class�� ����
	int value = 0;
	float voltage = 0.0;
	float temperature = 0.0;
	unsigned long boiling_start = 0;
	unsigned long boiling_end = 0;
};