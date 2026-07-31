package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.CreateClinicAccountRequest;
import com.hit.comemyway.dto.response.UserResponse;
import com.hit.comemyway.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Các API liên quan tới Admin")
@RequestMapping(ApiPath.API_V1)
public class AdminController {
  private final UserService userService;

  @Operation(summary = "Tạo tài khoản cho phòng khám",
      description = "gọi API này để tạo tài khoản cho phòng khám và gửi mật khẩu qua email")
  @PostMapping(UrlConstant.Admin.CREATE_CLINIC)
  public ResponseEntity<ApiResponse<UserResponse>> createClinicAccount(
      @Valid @RequestBody CreateClinicAccountRequest request) {
    UserResponse response = userService.createClinicAccount(request);

    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
