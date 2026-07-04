package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.AppointmentRequest;
import com.hit.comemyway.dto.response.AppointmentResponse;
import com.hit.comemyway.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
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
}
