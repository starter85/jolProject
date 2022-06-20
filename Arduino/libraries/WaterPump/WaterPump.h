#pragma once // ��������� �� ���� include
#define nDelay 5000

class WaterPump // WaterPump��� class ����
{
public: // �ܺο��� ���� ������ �Լ�
	// ������(constructor): ��� ����: ������ �� ȣ��Ǵ� �Լ�; �ʱ�ȭ; memory �Ҵ�
	WaterPump(void)
	{
	}
	// �ı���(desturctor): ����� ����; �ı��� �� ȣ��Ǵ� �Լ�; memory ����
	~WaterPump()
	{
	}

	void init(void)
	{
		pinMode(AA, OUTPUT);  // AA�� ��� ������ ����
		pinMode(AB, OUTPUT);  // AB�� ��� ������ ����
		pinMode(BA, OUTPUT);  // BA�� ��� ������ ����
		pinMode(BB, OUTPUT);  // BB�� ��� ������ ����
	}

	void waterPumpOn()
	{
		init();
		Serial.println("water pump ON");
		// ����Ĩ�� ���Ƶ鿩�� �����, �� ���̴°�
		digitalWrite(AA, HIGH);
		digitalWrite(AB, LOW);
		delay(2000);
  		//Ŀ���� ���Ƶ鿩�� ����� ,�� ��°�, ��ȯ����
		digitalWrite(BA, HIGH);
		digitalWrite(BB, LOW);
		
	}

	void waterPumpStop() {
		// ����Ĩ�� ���Ƶ鿩�� �����, �� ���̴°� ���� ����
		digitalWrite(AA, LOW);
		digitalWrite(AB, LOW);
		delay(5000); //�� ���� �ð�
		digitalWrite(BA, LOW);
		digitalWrite(BB, LOW);
		Serial.println("water pump OFF");
	}

protected: // �ܺο��� ��ȣ�Ǵ� ����/�Լ�
	// Property(����): class�� ����
	int AA = 4; // AA�� ������ ������ ��
	int AB = 5;  // AB�� ������ ������ ��
	int BA = 2;  // BA�� ������ ������ ��
	int BB = 3;  // BB�� ������ ������ ��
};