package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.AppointmentRequest;
import com.hit.comemyway.dto.response.AppointmentResponse;
import com.hit.comemyway.dto.response.AppointmentDetailResponse;
import com.hit.comemyway.dto.response.AppointmentDisplayResponse;
import com.hit.comemyway.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Appointment", description = "Các API liên quan tới đặt lịch khám")
@RequestMapping(ApiPath.API_V1)
public class AppointmentController {
  private final AppointmentService appointmentService;

  @Operation(summary = "Tạo lịch hẹn mới", description = "Người dùng gửi thông tin đặt lịch")
  @PostMapping(UrlConstant.Appointment.CREATE_APPOINTMENT)
  public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
      @Valid @RequestBody AppointmentRequest request) {
    AppointmentResponse response = appointmentService.createAppointment(request);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Lấy các lịch hẹn của người dùng",
      description = "Trả về danh sách lịch hẹn của người dùng")
  @GetMapping(UrlConstant.Appointment.GET_APPOINTMENT)
  public ResponseEntity<ApiResponse<List<AppointmentDisplayResponse>>> getAppointment() {
    List<AppointmentDisplayResponse> response = appointmentService.getUserAppointment();
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Chỉnh sửa lịch hẹn", description = "Người dùng thay đổi thông tin đặt lịch")
  @PostMapping(UrlConstant.Appointment.UPDATE_APPOINTMENT)
  public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(
      @PathVariable Long appointmentId, @Valid @RequestBody AppointmentRequest request) {
    AppointmentResponse response = appointmentService.updateAppointment(request, appointmentId);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Lấy chi tiết 1 lịch hẹn",
      description = "Trả về thông tin chi tiết 1 lịch hẹn")
  @GetMapping(UrlConstant.Appointment.GET_DETAIL)
  public ResponseEntity<ApiResponse<AppointmentDetailResponse>> getAppointmentDetail(
      @Parameter(description = "ID của lịch hẹn", required = true) @PathVariable Long id) {
    AppointmentDetailResponse response = appointmentService.getAppointmentDetail(id);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
