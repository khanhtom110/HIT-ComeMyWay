package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.ForgotPasswordRequest;
import com.hit.comemyway.dto.request.ResetPasswordRequest;
import com.hit.comemyway.dto.request.VerifyOtpRequest;
import com.hit.comemyway.service.ForgotPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(ApiPath.API_V1)
@RequiredArgsConstructor
@SecurityRequirements(value = {})
@Tag(name = "Authentication",
    description = "Endpoints cho user authentication, lấy lại password, và đăng ký")
public class ForgotPasswordController {

  private final ForgotPasswordService forgotPasswordService;

  @PostMapping(UrlConstant.Auth.FORGOT_PASSWORD)
  @Operation(summary = "Yêu cầu khôi phục mật khẩu",
      description = "Tạo và gửi mã OTP gồm 6 chữ số đến địa chỉ email đã đăng ký của người dùng.")
  public ResponseEntity<ApiResponse<Void>> sendOtp(
      @Valid @RequestBody ForgotPasswordRequest request) {
    String message = forgotPasswordService.sendOtpForgotPassword(request.email());

    return ResponseEntity.ok(ApiResponse.ok(message, null));
  }

  @PostMapping(UrlConstant.Auth.VERIFY_OTP)
  @Operation(summary = "Xác thực OTP khôi phục mật khẩu",
      description = "Kiểm tra mã OTP người dùng nhập vào. Nếu hợp lệ, hệ thống sẽ trả về một chuỗi token (có thời hạn) để thực hiện đổi mật khẩu.")
  public ResponseEntity<ApiResponse<Map<String, String>>> verifyOtp(
      @Valid @RequestBody VerifyOtpRequest request) {
    String resetToken = forgotPasswordService.verifyOtp(request.email(), request.otp());

    return ResponseEntity
        .ok(ApiResponse.ok(SuccessMessage.Auth.VERIFY_OTP_SUCCESS, Map.of("token", resetToken)));
  }

  @PostMapping(UrlConstant.Auth.RESET_PASSWORD)
  @Operation(summary = "Đặt lại mật khẩu mới",
      description = "Sử dụng token đã được cấp từ bước xác thực OTP để tiến hành đặt lại mật khẩu mới cho tài khoản.")
  public ResponseEntity<ApiResponse<Void>> resetPassword(
      @Valid @RequestBody ResetPasswordRequest request) {
    forgotPasswordService.resetPassword(request);

    return ResponseEntity.ok(ApiResponse.ok(SuccessMessage.Auth.RESET_PASSWORD_SUCCESS, null));
  }
}
