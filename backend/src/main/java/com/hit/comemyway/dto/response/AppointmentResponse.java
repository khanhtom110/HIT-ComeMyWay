package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Appointment;
import com.hit.comemyway.entity.BookingStatus;
import com.hit.comemyway.entity.BookingType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AppointmentResponse(
//@formatter:off
        @Schema(description = "ID lịch hẹn", example = "1")
        Long id,

        @Schema(description = "ID người dùng", example = "12")
        Long userId,

        @Schema(description = "ID phòng khám", example = "45")
        Long clinicId,

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

        @Schema(description = "Danh sách dịch vụ", example = "[\"Tiêm phòng\", \"Phẫu thuật\"]")
        List<String> services,

        @Schema(description = "Trạng thái lịch hẹn", example = "PENDING")
        BookingStatus status) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getUser().getId(),
                appointment.getClinic().getId(),
                appointment.getFullName(),
                appointment.getPhone(),
                appointment.getBookingType(),
                appointment.getHomeAddress(),
                appointment.getPetType(),
                appointment.getPetCondition(),
                appointment.getPetQuantity(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getServices().stream()
                           .map(service -> service.getName())
                           .toList(),
                appointment.getStatus()
        );
    }
}
