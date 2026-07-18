package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.RejectAppointmentRequest;
import com.hit.comemyway.dto.response.AppointmentResponse;
import com.hit.comemyway.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
@Tag(name = "Clinic Controller", description = "Các API nghiệp vụ của phòng khám")
public class ClinicController {
  private final AppointmentService appointmentService;


  @Operation(summary = "Lấy lịch hẹn đang cần duyệt",
      description = "Hiển thị danh sách lịch hẹn cần duyệt")
  @GetMapping(UrlConstant.Clinic.GET_APPOINTMENT_PENDING)
  public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPendingStatusAppointment() {
    List<AppointmentResponse> responses = appointmentService.getPendingStatusAppointment();
    return ResponseEntity.ok(ApiResponse.ok(responses));
  }

  @Operation(summary = "Duyệt lịch hẹn", description = "Phòng khám duyệt lịch hẹn của người dùng")
  @PostMapping(UrlConstant.Clinic.POST_APPOINTMENT_CONFIRMED)
  public ResponseEntity<ApiResponse<AppointmentResponse>> changeConfirmStatusAppointment(
      @PathVariable Long appointmentId) {
    AppointmentResponse response = appointmentService.confirmAppointmentStatus(appointmentId);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Từ chối lịch hẹn",
      description = "Phòng khám từ chối lịch hẹn của người dùng")
  @PostMapping(UrlConstant.Clinic.POST_APPOINTMENT_REJECTED)
  public ResponseEntity<ApiResponse<AppointmentResponse>> changeRejectStatusAppointment(
      @PathVariable Long appointmentId, @RequestBody RejectAppointmentRequest request) {
    AppointmentResponse response =
        appointmentService.rejectAppointmentStatus(appointmentId, request.rejectReason());
    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
