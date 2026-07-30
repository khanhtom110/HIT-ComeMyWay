package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.RejectAppointmentRequest;
import com.hit.comemyway.dto.response.AppointmentResponse;
import com.hit.comemyway.service.AppointmentService;
import com.hit.comemyway.dto.request.*;
import com.hit.comemyway.dto.response.*;
import com.hit.comemyway.service.ClinicService;
import com.hit.comemyway.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
@Tag(name = "Clinic Controller", description = "Các API nghiệp vụ của phòng khám")
public class ClinicController {
  private final ClinicService clinicService;
  private final UserService userService;
  private final AppointmentService appointmentService;


  @Operation(summary = "Lấy lịch hẹn đang cần duyệt",
      description = "Hiển thị danh sách lịch hẹn cần duyệt")
  @GetMapping(UrlConstant.Clinic.GET_APPOINTMENT_PENDING)
  public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPendingStatusAppointment() {
    List<AppointmentResponse> responses = appointmentService.getPendingStatusAppointment();
    return ResponseEntity.ok(ApiResponse.ok(responses));
  }

  @Operation(summary = "Lấy lịch hẹn đã duyệt",
      description = "Hiển thị danh sách lịch hẹn đã duyệt")
  @GetMapping(UrlConstant.Clinic.GET_APPOINTMENT_COMFIRMED)
  public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getConfirmedStatusAppointment() {
    List<AppointmentResponse> responses = appointmentService.getCofirmedStatusAppointment();
    return ResponseEntity.ok(ApiResponse.ok(responses));
  }

  @Operation(summary = "Lấy lịch hẹn đang đã từ chối",
      description = "Hiển thị danh sách lịch hẹn đã từ chối")
  @GetMapping(UrlConstant.Clinic.GET_APPOINTMENT_REJECTED)
  public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getRejectedStatusAppointment() {
    List<AppointmentResponse> responses = appointmentService.getRejectedStatusAppointment();
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

  @Operation(summary = "Lưu thông tin phòng khám lần đầu tiên",
      description = "Lưu thông tin khi phòng khám lần đầu đăng nhập vào app")
  @PostMapping(UrlConstant.Clinic.COMPLETE_PROFILE)
  public ResponseEntity<ApiResponse<CompleteClinicProfileResponse>> completeProfile(
      @Valid @RequestBody CompleteClinicProfileRequest request) {
    CompleteClinicProfileResponse response = clinicService.completeClinicProfile(request);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Lấy thông tin phòng khám", description = "Lấy thông tin phòng khám")
  @GetMapping(UrlConstant.Clinic.GET_PROFILE)
  public ResponseEntity<ApiResponse<CompleteClinicProfileResponse>> getCLinicProfile() {
    CompleteClinicProfileResponse response = clinicService.getClinicProfile();
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Thay đổi thông tin phòng khám nếu cần",
      description = "Thay đổi thông tin phòng khám nếu cần")
  @PostMapping(UrlConstant.Clinic.UPDATE_PROFILE)
  public ResponseEntity<ApiResponse<CompleteClinicProfileResponse>> updateProfile(
      @Valid @RequestBody CompleteClinicProfileRequest request) {
    CompleteClinicProfileResponse response = clinicService.updateClinicProfile(request);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Đổi mật khẩu",
      description = "Đổi mật khẩu cho phòng khám khi đăng nhập lần đầu")
  @PostMapping(UrlConstant.Clinic.CHANGE_PASSWORD)
  public ResponseEntity<ApiResponse<UserResponse>> changeFirstTimePassword(
      @Valid @RequestBody ChangePasswordRequest request) {
    UserResponse response = userService.changeFirstTimePassword(request);

    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
