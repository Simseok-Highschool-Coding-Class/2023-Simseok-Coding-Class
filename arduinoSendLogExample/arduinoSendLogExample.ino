#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>


// 인터넷 연결 설정
const char* ssid = "wifi-test";
const char* password = "19950620";
const char* baseUrl = "http://ping2.doky.space";


void setup() {

  // 시리얼 통신 설정
  Serial.begin(115200);
  Serial.print("ON\n");
  pinMode(4, OUTPUT);

  for(int i=0; i<10; i++) {

    Serial.print("ON\n");
    digitalWrite(4, HIGH);
    delay(500);

    Serial.println("OFF\n");
    digitalWrite(4, LOW);
    delay(500);

  }



  delay(30000);



  
  // 와이파이 연결
  connectToWifi(ssid, password);
  delay(3000);

  // 통신 연결
  HTTPClient client = createClient(baseUrl);

  // 로그 보내기
  for(int i=0 ; i < 5; i++) {
    sendLog(&client, "test-arr", 1024);
    delay(1000);
  }

  // 연결 종료
  closeClient(&client);
}


void loop() {
  // 아무것도 안함
}



//////////////////////////////////////////////////////
//////////////////////////////////////////////////////
//////////////////////////////////////////////////////



void connectToWifi(const char* ssid, const char* password) {
  Serial.println();
  Serial.print("CONNECTING TO ");
  Serial.println(ssid);

  // 와이파이 이름과 비밀번호를 통해 WIFI연결을 시작하겠다 // WL_CONNECTED라는 값을 돌려준다
  WiFi.begin(ssid, password); 

  // 네트워크의 연결 상태, 8개의 리턴값 / STATUS와 WL_CONNECTED 값이 같은지를 통해 제대로 연결이 되있는지를 확인할 수 있다
  while(WiFi.status() != WL_CONNECTED){ 
    delay(500);
    Serial.print(".");  
  }
  Serial.println();
  Serial.println("Wifi connected!");
}

HTTPClient createClient(const char* url) {
  WiFiClient client;
  HTTPClient http;

  Serial.printf("[createClient] begin... %s\n", url);
  http.begin(client, url);

  return http;
}

void closeClient(HTTPClient* client) {
  Serial.print("[closeClient] end\n");
  client->end();
}

void sendLog(HTTPClient* client, const char* name, int value) {
  Serial.print("[sendLog] POST... number value\n");
  String body = "{\"name\": \"" + String(name) + "\", \"log\": " + value + "}";
  sendLogPOST(client, body.c_str());
}

void sendLog(HTTPClient* client, const char* name, const char* value) {
  Serial.print("[sendLog] POST... string value\n");
  String body = "{\"name\": \"" + String(name) + "\", \"log\": " + value + "}";
  sendLogPOST(client, body.c_str());
}

void sendLogPOST(HTTPClient* client, const char* body) {
  // start connection and send HTTP header
  client->addHeader("Content-Type", "application/json");
  int httpCode = client->POST(body);

  // httpCode will be negative on error
  if (httpCode > 0) {
    // HTTP header has been send and Server response header has been handled
    Serial.printf("[sendLog] POST... code: %d\n", httpCode);

    // file found at server
    if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
      String payload = client->getString();
      Serial.println(payload);
    }
  } else {
    Serial.printf("[sendLog] POST... failed, %d error: %s\n", httpCode, client->errorToString(httpCode).c_str());
  }
}
