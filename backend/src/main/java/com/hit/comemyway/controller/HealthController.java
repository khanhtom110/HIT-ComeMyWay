package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.API_V1)
public class HealthController {
  @Operation(summary = "Kiểm tra trạng thái Server",
      description = "API dành riêng cho UptimeRobot ping để giữ server luôn thức")
  @GetMapping(UrlConstant.Public.HEALTH_CHECK)
  public ResponseEntity<ApiResponse<String>> healthCheck() {
    return ResponseEntity.ok(ApiResponse.ok("Server is awake"));
  }
}
