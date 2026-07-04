package com.hit.comemyway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ComemywayApplication {

  public static void main(String[] args) {
      TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
    SpringApplication.run(ComemywayApplication.class, args);
  }

}
