#include <WiFi.h>
#include <WebServer.h>

const char* ssid = "ESP32_CAM";
const char* password = "12345678";

WebServer server(80);

void handlePredict() {
  // Fake AI result
  String json = "{";
  json += "\"type\":\"nut\",";
  json += "\"status\":\"bad\",";
  json += "\"confidence\":0.73";
  json += "}";

  server.send(200, "application/json", json);
}

void setup() {
  Serial.begin(115200);

  // 🔥 Start Access Point
  WiFi.softAP(ssid, password);

  Serial.println("ESP32 AP Started");
  Serial.print("IP Address: ");
  Serial.println(WiFi.softAPIP()); // usually 192.168.4.1

  server.on("/predict", handlePredict);
  server.begin();
}

void loop() {
  server.handleClient();
}
