package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.*;
import com.hit.comemyway.dto.response.UpdateUserResponse;
import com.hit.comemyway.dto.response.UserResponse;
import com.hit.comemyway.service.AppointmentReminderService;
import com.hit.comemyway.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "Các API liên quan tới quản lý tài khoản người dùng")
@RequestMapping(ApiPath.API_V1)
public class UserController {

  private final UserService userService;

  @Operation(summary = "Cập nhật Firebase Device Token",
      description = "gọi API này ngay sau khi đăng nhập thành công hoặc và người dùng cấp quyền cho phép thông báo.")
  @PostMapping(UrlConstant.User.DEVICE_TOKEN)
  public ResponseEntity<ApiResponse<Void>> updateDeviceToken(
      @Valid @RequestBody DeviceTokenRequest request) {
    userService.updateDeviceToken(request.deviceToken());

    return ResponseEntity.ok(ApiResponse.ok(null));
  }

  @Operation(summary = "Lấy thông tin user để cập nhật")
  @GetMapping(UrlConstant.User.GET_PROFILE)
  public ResponseEntity<ApiResponse<Object>> getUserInformation() {
    return ResponseEntity.ok(ApiResponse.ok(userService.getProfile()));
  }

  @Operation(summary = "Cập nhật thông tin user")
  @PostMapping(UrlConstant.User.UPDATE_PROFILE)
  public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUserInformation(
      @Valid @RequestBody UpdateUserRequest request, @PathVariable Long id) {
    UpdateUserResponse response = userService.updateUserProfile(request, id);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Cập nhật mật khẩu user")
  @PostMapping(UrlConstant.User.CHANGE_PASSWORD)
  public ResponseEntity<ApiResponse<Void>> updateUserPassword(
      @Valid @RequestBody ChangeUserPasswordRequest request) {
    userService.updateUserPassword(request);
    return ResponseEntity.ok(ApiResponse.ok(null));
  }

  @Operation(summary = "Lấy link locket người dùng")
  @GetMapping(UrlConstant.User.USER_LOCKET_LINK)
  public ResponseEntity<ApiResponse<String>> getUserLocketLink() {
    String response = userService.getLocketLink();
    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
