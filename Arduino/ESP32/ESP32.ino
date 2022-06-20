#define VOLT_PIN (A0)
#include <DHT.h>
#include <WiFi.h> // ESP32 와이파이 헤더파일
#include <FirebaseESP32.h>  // ESP32로 파이어베이스 라이브러리

#define WIFI_SSID "kkd96" // 와이파이(핫스팟) 아이디 , (변경 가능 부분)
#define WIFI_PASSWORD "11111111" // 와이파이(핫스팟) 비밀번호 (변경 가능 부분)

// 파이어베이스 주소
#define FIREBASE_HOST "gocook-2bf09-default-rtdb.firebaseio.com" //(변경 가능 부분)
// 파이어베이스 비밀번호 ( 환경설정-서비스계정-데이터베이스 비밀번호 )
#define FIREBASE_AUTH "7fEoYNIWgndGwp7jKMhfq247VbeROKqTL4WS1iiC"  //(변경 가능 부분)

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseJson json;
FirebaseConfig config;
int n=0;

void setup() {
  Serial.begin(115200); // 통신 속도 ( 와이파이 통신을 하려면 115200이 최적이라함)
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.println("WIFI not Connected");
    delay(1000);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
  Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  Firebase.setReadTimeout(fbdo, 1000*60);
  Firebase.setwriteSizeLimit(fbdo, "tiny");

  Serial.println("=================================");
  Serial.println("Connected...");
  dht1.begin();
  dht2.begin();
}

void loop() {
  // n++;
  //String h1 = String(h);

  int nVolt = analogRead(VOLT_PIN); // 0~1023 <=> 0~5V
  double volt = (nVolt/4095.0)*3.3; // Voltmeter
  String sVolt = String(volt);
  Serial.print("Volt : ");
  Serial.println(volt);
  delay(200);
  
  // 면,소스 무게값으로 바꿀 부분, 스트링으로 변환해서 파이어베이스에 넣어야함.
  Firebase.setString(fbdo,"noodleWeight",sVolt);  // 면 무게
  Firebase.setString(fbdo,"sourceWeight",sVolt);  // 소스무게
  //Firebase.setFloat(fbdo,"number ",n);  // 횟수(카운트)
  delay(200);
}
