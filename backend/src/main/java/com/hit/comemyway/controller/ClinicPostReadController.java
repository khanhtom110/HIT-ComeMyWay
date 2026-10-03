package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.dto.response.ClinicPostListItemResponse;
import com.hit.comemyway.dto.response.ClinicPostResponse;
import com.hit.comemyway.dto.response.CursorResponse;
import com.hit.comemyway.service.ClinicPostReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/clinic-posts")
@SecurityRequirements
@Tag(name = "Clinic Posts", description = "Người dùng xem bài viết của phòng khám")
public class ClinicPostReadController {
  private final ClinicPostReadService service;

  @GetMapping
  @Operation(summary = "Danh sách bài viết",
      description = "Bài đăng hiển thị ngay, mới nhất trước; "
          + "dùng lastPostId của trang trước để lấy trang tiếp theo. limit từ 1 đến 50.")
  public ApiResponse<CursorResponse<ClinicPostListItemResponse>> list(
      @RequestParam(required = false) Long lastPostId,
      @RequestParam(defaultValue = "10") int limit) {
    return ApiResponse.ok(service.list(lastPostId, limit));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Chi tiết bài viết",
      description = "Trả toàn bộ ảnh; bài không tồn tại hoặc đã xóa trả 404.")
  public ApiResponse<ClinicPostResponse> detail(@PathVariable long id) {
    return ApiResponse.ok(service.detail(id));
  }
}
