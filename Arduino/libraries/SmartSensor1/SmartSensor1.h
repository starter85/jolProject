// Arduino Firmware for SensorHub
#pragma once //                   include
#include <SoftwareSerial.h>
#include <HEAT_PAD.h>
#include <WaterPump.h>
#include <DCMotor.h>


class SmartSensor1 // SmartSensor1 ̶   class     
{
public: //  ܺο                 Լ 
	//       (constructor):         ;           ȣ  Ǵ   Լ ;  ʱ ȭ
	SmartSensor1(void)
	{}
	//  ı   (desturctor:           ;  ı       ȣ  Ǵ   Լ 
	~SmartSensor1()
	{}

	void init(void) //  Լ   ʱ ȭ
	{
	}

	void BTN_run(void)
	{
		//String recipe = parseCmdToToken();
		//getCmd();
		SoftwareSerial BT(pinTx, pinRx);  // 블루투스 통신의 송수신 핀 설정
		BT.begin(9600);  // 블루투스 통신 초기화 (속도= 9600 bps)
		//Serial.println("AT command");
		delay(100);
		while (BT.available())
		{  // bluetooth 수신값이 있는가?, 있으면 계속 수신 받기
			temp = (char)BT.read(); // 수신 값 char로 캐스팅(변환) 후 문자단위로 읽기
			if (temp == 'e') { // 문자열 마지막에 e를 넣었다. 문자 e 가 인식되면 반복문 종료( 안드로이드에서 아두이노로 송신하는 값 : "보내려는 문자열 + e") ex) nt 10 ct 10e // 문자를 1개 보내면 길이 3이 출력됌. 즉, null값과 다른 1개의 값이 더 들어간다는 말임
				break;
			}
			recipe += temp; //한 문자씩 더하기
			delay(5); // 수신 에러 일어나지 않도록 딜레이
		}
		if (!recipe.equals("")){
			//빈 문자열이 아니면 실행
			len_recipe = recipe.length(); //수신 받은 자열 길이
			Serial.print("recived data : ");
			Serial.println(recipe);
			Serial.print("data length : ");
			
			Serial.println(len_recipe);
			if (recipe.indexOf(" ") != -1) {
				blank1 = recipe.indexOf(" ");
				blank2 = recipe.indexOf(" ", blank1 + 1);
				blank3 = recipe.indexOf(" ", blank2 + 1);
				nt_sec = recipe.substring(blank1 + 1, blank2);
				ct_sec = recipe.substring(blank3 + 1, len_recipe);
				Serial.println(nt_sec);
				Serial.println(ct_sec);
				delay(500);

				// 요리 시작
				m_heat_pad.WaterHeatPad_On(); // 면 끓이는 열판 작동
				m_waterpump.waterPumpOn();
				Serial.print("nt_sec : ");
				Serial.println(nt_sec.toInt() * 1000);
				delay(nt_sec.toInt() * 1000);	//nt 시간만큼 면 끓이기
				m_heat_pad.WaterHeatPad_Off();		// 면 끓이는 시간(nt_sec) 만큼 지났으므로 열판 끄기
				m_waterpump.waterPumpStop();
				delay(5000);
				//요리 시작
				m_heat_pad.CookHeatPad_On(); // 면 끓이는 열판 작동

				// 재료1 (마늘) 떨어뜨리기
				Serial.println("drop garlic");
				Serial1.println("1");			// 서보모터 돌리는 아두이노메가로 통신
				delay(5000);

				// 모터 작동
				m_dcMotor.cook();

				// 소스 떨어뜨리기
				Serial.println("drop source");
				Serial1.println("s");			// 서보모터 돌리는 아두이노메가로 통신
				delay(5000);

				// 면 떨어뜨리기
				Serial.println("drop noodle");
				Serial1.println("n");			// 서보모터 돌리는 아두이노메가로 통신
				delay(5000);

				// 요리시간만큼 모터 돌리기 위해서 딜레이
				delay(ct_sec.toInt() * 1000); 
				m_dcMotor.stopCook();
				m_heat_pad.CookHeatPad_Off(); // 면 끓이는 열판 작동

				// 재료2 (페퍼론치노) 떨어뜨리기
				Serial.println("drop pepperoncino");
				Serial1.println("2");			// 서보모터 돌리는 아두이노메가로 통신
				delay(5000);

				// 재료 3(파마산가루) 떨어뜨리기
				Serial.println("drop Parmesan");
				Serial1.println("3");			// 서보모터 돌리는 아두이노메가로 통신
				delay(5000);
			}
			else if (recipe.indexOf(" ") == -1) {  // 띄어 쓰기가 없을 때 -1 반환 , off 버튼 눌렀을 때
				m_heat_pad.WaterHeatPad_Off();		// 면 끓이는 시간(nt_sec) 만큼 지났으므로 열판 끄기
				m_heat_pad.CookHeatPad_Off(); // 면 끓이는 열판 작동
				m_dcMotor.stopCook();
				// 초기화
				blank1 = 0;
				blank2 = 0;
				blank3 = 0;
				cmd1 = "";
				cmd2 = "";
				nt_sec = "";
				ct_sec = "";
				recipe = "";
				len_recipe = 0;
				dc_moter_on = "";
			}
		}
	}

protected: //  ܺο      ȣ Ǵ      / Լ 
// Property(    , Ӽ ): class         
	HEAT_PAD m_heat_pad;
	WaterPump m_waterpump;
	DCMotor m_dcMotor;

	const int pinTx = 11;  // 블루투스 TX 연결 핀 번호, 송신
	const int pinRx = 10;  // 블루투스 RX 연결 핀 번호, 수신
	String recipe;
	int len_recipe;
	char temp;
	char dc_moter_on;
	int blank1;
	int blank2;
	int blank3;
	String cmd1;
	String cmd2;
	String nt_sec;
	String ct_sec;
};