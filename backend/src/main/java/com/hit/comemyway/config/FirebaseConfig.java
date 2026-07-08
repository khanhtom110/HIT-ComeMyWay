package com.hit.comemyway.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

  @PostConstruct
  public void initialize() {
    try {
      InputStream serviceAccount;

      // Đường dẫn mặc định của Render khi cấu hình Secret Files
      File renderSecret = new File("/etc/secrets/firebase-service-account.json");

      if (renderSecret.exists()) {
        // 1. Kịch bản chạy trên server Render
        serviceAccount = new FileInputStream(renderSecret);
        System.out.println("Đang khởi tạo Firebase từ Render Secret File...");
      } else {
        // 2. Kịch bản chạy Local ở máy dev (đọc từ src/main/resources)
        serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();
        System.out.println("Đang khởi tạo Firebase từ Local Classpath...");
      }

      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
      }

    } catch (IOException e) {
      System.err.println("Lỗi khởi tạo Firebase: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
