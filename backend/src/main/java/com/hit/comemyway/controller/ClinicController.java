package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.response.ClinicDetailResponse;
import com.hit.comemyway.dto.response.ClinicSearchResponse;
import com.hit.comemyway.dto.response.ClinicSuggestionResponse;
import com.hit.comemyway.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
@Validated
@Tag(name = "Clinic Controller", description = "Các API tìm kiếm phòng khám")
@SecurityRequirements(value = {})
public class ClinicController {
  private final ClinicService clinicService;

  @Operation(summary = "Tìm kiếm phòng khám",
      description = "Tìm kiếm theo từ khóa hoặc vị trí địa lý với bán kính cho trước")
  @GetMapping(UrlConstant.Public.CLINIC_SEARCH)
  public ResponseEntity<ApiResponse<List<ClinicSearchResponse>>> searchClinics(
      @Parameter(description = "Từ khóa tìm kiếm (tên, địa chỉ, hoặc dịch vụ)")
      @RequestParam(required = false) String keyword,

      @Parameter(description = "Vĩ độ của người dùng") @RequestParam(required = false)
      @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,

      @Parameter(description = "Kinh độ của người dùng") @RequestParam(required = false)
      @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,

      @Parameter(description = "Bán kính tìm kiếm (km)") @RequestParam(defaultValue = "10.0")
      @Min(1) Double radius,

      @Parameter(description = "Số lượng phòng khám tối đa trên một trang tìm kiếm")
      @RequestParam(defaultValue = "20") @Min(1) @Max(30) int limit) {
    List<ClinicSearchResponse> response =
        clinicService.findClinics(keyword, latitude, longitude, radius, limit);

    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Gợi ý phòng khám",
      description = "Trả về danh sách gợi ý khi người dùng nhập các ký tự")
  @GetMapping(UrlConstant.Public.CLINIC_SUGGESTION)
  public ResponseEntity<ApiResponse<List<ClinicSuggestionResponse>>> getSuggestions(
      @Parameter(description = "Từ khóa") @RequestParam(required = false) String keyword,

      @Parameter(description = "Số lượng gợi ý (tối đa 10)") @RequestParam(defaultValue = "5")
      @Min(1) @Max(10) int limit) {
    List<ClinicSuggestionResponse> response = clinicService.getSuggestions(keyword, limit);

    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Lấy chi tiết phòng khám",
      description = "Trả về thông tin chi tiết của một phòng khám")
  @GetMapping(UrlConstant.Public.CLINIC_DETAIL)
  public ResponseEntity<ApiResponse<ClinicDetailResponse>> getClinicDetailById(
      @Parameter(description = "ID của phòng khám", required = true) @PathVariable Long clinicId) {
    ClinicDetailResponse response = clinicService.getClinicById(clinicId);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Gợi ý phòng khám",
      description = "Trả về danh sách gợi ý khi người dùng nhấn vào ô tìm kiếm")
  @GetMapping(UrlConstant.Public.GET_SUGGESTION)
  public ResponseEntity<ApiResponse<List<ClinicSuggestionResponse>>> getClinicSuggestion(
      @Parameter(description = "Vĩ độ của người dùng") @RequestParam(required = false)
      @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,

      @Parameter(description = "Kinh độ của người dùng") @RequestParam(required = false)
      @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,

      @Parameter(description = "Số lượng phòng khám tối đa trên một trang gợi ý")
      @RequestParam(defaultValue = "5") @Min(1) @Max(8) int limit) {
    List<ClinicSuggestionResponse> response =
        clinicService.getClinicSuggestions(true, latitude, longitude, 10.0, limit);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
