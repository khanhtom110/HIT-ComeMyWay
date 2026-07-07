package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.DeviceTokenRequest;
import com.hit.comemyway.service.AppointmentReminderService;
import com.hit.comemyway.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
}
