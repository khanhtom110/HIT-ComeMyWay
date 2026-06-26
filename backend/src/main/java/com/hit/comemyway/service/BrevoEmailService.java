package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.exception.extended.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {

  @Value("${brevo.api.key}")
  private String apiKey;

  @Value("${brevo.sender.email}")
  private String senderEmail;

  public void sendOtpEmail(String toEmail, String subject, String htmlContent) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "https://api.brevo.com/v3/smtp/email";

    // Setup API Key into Header to access Brevo
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("api-key", apiKey);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    // Map data
    Map<String, Object> body = Map.of("sender", Map.of("name", "Come My Way", "email", senderEmail),
        "to", List.of(Map.of("email", toEmail)), "subject", subject, "htmlContent", htmlContent);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

    // Push Request into port HTTPS (443) - Render khong chan cong nay
    try {
      ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    } catch (Exception e) {
      throw new AppException(500, ErrorMessage.Auth.SEND_MAIL_FAIL);
    }
  }
}
