package com.hit.comemyway.controller;

import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.dto.request.ForgotPasswordRequest;
import com.hit.comemyway.dto.request.ResetPasswordRequest;
import com.hit.comemyway.dto.request.VerifyOtpRequest;
import com.hit.comemyway.service.ForgotPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class ForgotPasswordController {

  @Autowired
  private ForgotPasswordService forgotPasswordService;

  @PostMapping("/forgot-password")
  public ResponseEntity<?> sendOtp(@Valid @RequestBody ForgotPasswordRequest request) {
    String message = forgotPasswordService.sendOtpForgotPassword(request.email());

    return ResponseEntity.ok(Map.of("message", message));
  }

  @PostMapping("/verify-otp")
  public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
    String resetToken = forgotPasswordService.verifyOtp(request.email(), request.otp());

    return ResponseEntity
        .ok(Map.of("message", SuccessMessage.Auth.VERIFY_OTP_SUCCESS, "token", resetToken));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    forgotPasswordService.resetPassword(request);
    return ResponseEntity.ok(Map.of("message", SuccessMessage.Auth.RESET_PASSWORD_SUCCESS));
  }
}
