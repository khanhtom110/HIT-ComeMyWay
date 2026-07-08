package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Appointment;
import com.hit.comemyway.entity.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDisplayResponse(
//@formatter:off
        @Schema(description = "ID lịch hẹn", example = "1")
        Long id,

        @Schema(description = "ID phòng khám", example = "45")
        Long clinicId,

        @Schema(description = "Tên phòng khám", example = "Phòng khám Thú y Cộng đồng")
        String name,

        @Schema(description = "Địa chỉ phòng khám", example = "83 Giải Phóng, P. Đồng Tâm")
        String address,

        @Schema(description = "URL ảnh đại diện", example = "https://example.com/clinic.jpg")
        String thumbnailUrl,

        @Schema(description = "Ngày khám", example = "2026-07-05")
        LocalDate appointmentDate,

        @Schema(description = "Giờ khám", example = "09:30:00")
        LocalTime appointmentTime,

        @Schema(description = "Trạng thái lịch hẹn", example = "PENDING")
        BookingStatus status) {
    public static AppointmentDisplayResponse from (Appointment appointment) {
        return new AppointmentDisplayResponse (
                appointment.getId(),
                appointment.getClinic().getId(),
                appointment.getClinic().getName(),
                appointment.getClinic().getAddress(),
                appointment.getClinic().getThumbnailUrl(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus()
        );
    }
}
