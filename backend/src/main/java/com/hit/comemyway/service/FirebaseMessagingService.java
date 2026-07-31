package com.hit.comemyway.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {
  public void sendNotification(String deviceToken, String title, String body) {
    try {
      Notification notification = Notification.builder().setTitle(title).setBody(body).build();

      Message message =
          Message.builder().setToken(deviceToken).setNotification(notification).build();

      FirebaseMessaging.getInstance().send(message);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
