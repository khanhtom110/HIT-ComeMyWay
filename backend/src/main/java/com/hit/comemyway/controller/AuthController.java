package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.base.CommonMessage;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.*;
import com.hit.comemyway.dto.response.LoginResponse;
import com.hit.comemyway.dto.response.RegisterResponse;
import com.hit.comemyway.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
@Tag(name = "Authentication", description = "Các API Quản lý đăng nhập, đăng ký và Token")
@SecurityRequirements(value = {})
public class AuthController {
  private final AuthService authService;

  @PostMapping(UrlConstant.Auth.LOGIN)
  @Operation(summary = "Đăng nhập hệ thống",
      description = "Xác thực thông tin người dùng và trả về Access Token cùng Refresh Token để sử dụng cho các API yêu cầu bảo mật.")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);

    return ResponseEntity.ok(ApiResponse.ok(SuccessMessage.Auth.LOGIN_SUCCESS, response));
  }

  @PostMapping(UrlConstant.Auth.REGISTER)
  @Operation(summary = "Yêu cầu đăng ký tài khoản",
      description = "Gửi thông tin đăng ký cơ bản. Hệ thống sẽ lưu tạm thông tin và gửi một mã OTP về email để xác thực danh tính.")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
    authService.initRegister(request);

    return ResponseEntity.ok(ApiResponse.ok("Vui lòng kiểm tra email để lấy mã OTP.", null));
  }

  @PostMapping(UrlConstant.Auth.VERIFY_REGISTER_OTP)
  @Operation(summary = "Xác thực OTP và hoàn tất đăng ký",
      description = "Nhập mã OTP đã nhận qua email. Nếu mã hợp lệ, hệ thống sẽ chính thức tạo tài khoản và lưu vào cơ sở dữ liệu.")
  public ResponseEntity<ApiResponse<RegisterResponse>> verifyRegister(
      @Valid @RequestBody VerifyOtpRequest request) {

    RegisterResponse response = authService.verifyRegister(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.created(SuccessMessage.Auth.REGISTER_SUCCESS, response));
  }

  @PostMapping(UrlConstant.Auth.REFRESH_TOKEN)
  @Operation(summary = "Cấp lại Access Token mới",
      description = "Sử dụng Refresh Token (còn hiệu lực) để yêu cầu cấp lại cặp Access Token và Refresh Token mới mà không cần đăng nhập lại.")
  public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
      @Valid @RequestBody RefreshTokenRequest request) {
    LoginResponse response = authService.refreshToken(request);
    return ResponseEntity.ok(ApiResponse.ok(CommonMessage.SUCCESS, response));
  }

  @PostMapping(UrlConstant.User.LOGOUT)
  @SecurityRequirement(name = "BearerAuth")
  @Operation(summary = "Đăng xuất hệ thống",
      description = "Vô hiệu hóa (invalidate) Refresh Token hiện tại, đưa token này vào danh sách đen để chặn các yêu cầu làm mới token trong tương lai.")
  public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
    authService.logout(request);

    return ResponseEntity.ok(ApiResponse.ok(SuccessMessage.Auth.LOGOUT_SUCCESS, null));
  }
}
