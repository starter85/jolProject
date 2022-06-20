# pragma once // ��������� �ѹ��� include

class DCMotor // DCMotor��� class ����
{
public: // �ܺο��� ���� ������ �Լ�
	// ������(constructor): ��� ����: ������ �� ȣ��Ǵ� �Լ�; �ʱ�ȭ; memory �Ҵ�
	DCMotor(void){}
	// �ı���(destructor): ����� ����; �ı��� �� ȣ��Ǵ� �Լ�; memory ����
	~DCMotor(){}
	
	// �ʱ�ȭ
	void init(void)
	{
		pinMode(IN1Pin, OUTPUT);
		pinMode(IN2Pin, OUTPUT);
		analogWrite(ENPin, 5); // 0-255 motor speed.
		Serial.println("dcmotor init");
    }
	// �丮����
	void cook(void)
	{
		init();
		digitalWrite(IN1Pin, HIGH);
		digitalWrite(IN2Pin, LOW);
		Serial.println("dcmotor on, start cooking");
	}
	// �丮����
	void stopCook(){
		digitalWrite(IN1Pin, LOW);
		digitalWrite(IN2Pin, LOW);
		Serial.println("dcmotor stop");
	}

protected: // �ܺο��� ��ȣ�Ǵ� ����/�Լ�
	// Property(����): class�� ����
	int IN1Pin = 44; //  Pin ��ȣ: 44
	int IN2Pin = 45; //  Pin ��ȣ: 45
	int ENPin = 46;
};