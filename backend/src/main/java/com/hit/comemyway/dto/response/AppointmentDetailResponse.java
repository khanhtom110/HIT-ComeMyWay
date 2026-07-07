package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Appointment;
import com.hit.comemyway.entity.BookingStatus;
import com.hit.comemyway.entity.BookingType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDetailResponse(
//@formatter:off
        @Schema(description = "ID người dùng", example = "12")
        Long userId,

        @Schema(description = "ID phòng khám", example = "45")
        Long clinicId,

        @Schema(description = "Tên phòng khám", example = "Phòng khám Thú y Cộng đồng")
        String name,

        @Schema(description = "Địa chỉ phòng khám", example = "83 Giải Phóng, P. Đồng Tâm")
        String address,

        @Schema(description = "URL ảnh đại diện", example = "https://example.com/clinic.jpg")
        String thumbnailUrl,

        @Schema(description = "Họ và tên", example = "Nguyễn Công Phượng")
        String fullName,

        @Schema(description = "Số điện thoại", example = "0912345678")
        String phone,

        @Schema(description = "Loại đặt lịch", example = "AT_CLINIC")
        BookingType bookingType,

        @Schema(description = "Địa chỉ tại nhà", example = "Số 1, đường Nhổn, Hà Nội")
        String homeAddress,

        @Schema(description = "Loại thú cưng", example = "Chó")
        String petType,

        @Schema(description = "Tình trạng thú cưng", example = "Thú cưng bị sốt nhẹ")
        String petCondition,

        @Schema(description = "Số lượng thú cưng", example = "1")
        Integer petQuantity,

        @Schema(description = "Ngày khám", example = "2026-07-05")
        LocalDate appointmentDate,

        @Schema(description = "Giờ khám", example = "09:30:00")
        LocalTime appointmentTime,

        @Schema(description = "Trạng thái lịch hẹn", example = "PENDING")
        BookingStatus status) {
    public static AppointmentDetailResponse from (Appointment appointment) {
        return new AppointmentDetailResponse(
                appointment.getUser().getId(),
                appointment.getClinic().getId(),
                appointment.getClinic().getName(),
                appointment.getClinic().getAddress(),
                appointment.getClinic().getThumbnailUrl(),
                appointment.getFullName(),
                appointment.getPhone(),
                appointment.getBookingType(),
                appointment.getHomeAddress(),
                appointment.getPetType(),
                appointment.getPetCondition(),
                appointment.getPetQuantity(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus()
        );
    }
}
