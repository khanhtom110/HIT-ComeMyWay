package com.hit.comemyway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

  @Value("${gemini.api.url}")
  private String apiUrl;

  @Value("${gemini.api.key}")
  private String apiKey;

  public String callGemini(String promptMessage) {
    RestTemplate restTemplate = new RestTemplate();
    String fullUrl = apiUrl + "?key=" + apiKey;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Escape các ký tự đặc biệt để không bị lỗi cú pháp JSON payload
    String sanitizedPrompt = promptMessage.replace("\"", "\\\"").replace("\n", "\\n");

    String requestBody = """
        {
          "contents": [{
            "parts": [{
              "text": "%s"
            }]
          }]
        }
        """.formatted(sanitizedPrompt);

    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);

      // Parse JSON trả về từ Google để lấy chính xác phần text của AI
      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = mapper.readTree(response.getBody());
      return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text")
          .asText();

    } catch (Exception e) {
      return "{\"advice\": \"Lỗi kết nối tới hệ thống AI: " + e.getMessage()
          + "\", \"serviceName\": \"\"}";
    }
  }
}
