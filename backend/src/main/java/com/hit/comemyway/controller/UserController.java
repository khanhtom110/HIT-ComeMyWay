package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.API_V1)
@Tag(name = "User Management", description = "Phân nhóm các API liên quan đến Người dùng")
public class UserController {

  @Operation(summary = "Lấy thông tin hồ sơ cá nhân",
      description = "API này yêu cầu người dùng phải đăng nhập trước và truyền Bearer Token hợp lệ.")
  @GetMapping(UrlConstant.User.GET_PROFILE) // Kết quả endpoint thực tế: /api/v1/user/profile
  public ApiResponse<UserResponse> getProfile() {

    // Tạo Mock Data trực tiếp để Android có mẫu cấu hình JSON làm việc ngay hôm nay
    UserResponse mockProfile = new UserResponse(1L, "khanhtom", "khanh@gmail.com");

    return ApiResponse.ok("Lấy dữ liệu hồ sơ thành công", mockProfile);
  }
}
