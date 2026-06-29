package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.DefaultSuggestClinicsRequest;
import com.hit.comemyway.dto.request.SearchClinicsRequest;
import com.hit.comemyway.dto.request.SuggestClinicsRequest;
import com.hit.comemyway.dto.response.ClinicDetailResponse;
import com.hit.comemyway.dto.response.ClinicSearchResponse;
import com.hit.comemyway.dto.response.ClinicSuggestionResponse;
import com.hit.comemyway.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
      @Valid SearchClinicsRequest request) {
    List<ClinicSearchResponse> response = clinicService.findClinics(request.keyword(),
        request.latitude(), request.longitude(), request.radius(), request.limit());

    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Gợi ý phòng khám",
      description = "Trả về danh sách gợi ý khi người dùng nhập các ký tự")
  @GetMapping(UrlConstant.Public.CLINIC_SUGGESTION)
  public ResponseEntity<ApiResponse<List<ClinicSuggestionResponse>>> getSuggestions(
      @Valid SuggestClinicsRequest request) {
    List<ClinicSuggestionResponse> response =
        clinicService.getSuggestions(request.keyword(), request.limit());

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
      @Valid DefaultSuggestClinicsRequest request) {
    List<ClinicSuggestionResponse> response = clinicService.getClinicSuggestions(true,
        request.latitude(), request.longitude(), 10.0, request.limit());
    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
