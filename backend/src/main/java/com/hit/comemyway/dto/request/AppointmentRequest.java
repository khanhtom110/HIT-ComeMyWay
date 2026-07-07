package com.hit.comemyway.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hit.comemyway.constant.CommonConstant;
import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.entity.BookingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AppointmentRequest(
// @formatter:off
        @Schema(description = "ID phòng khám", example = "1")
        @NotNull(message = ErrorMessage.NOT_BLANK_FIELD)
        Long clinicId,

        @Schema(description = "Họ và tên khách hàng", example = "Nguyễn Công Phượng")
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Size(min = 4, max = CommonConstant.FULLNAME_LENGTH, message = ErrorMessage.INVALID_FORMAT_FULLNAME)
        String fullName,

        @Schema(description = "Số điện thoại liên hệ", example = "0912345678")
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Pattern(regexp = CommonConstant.PHONE_REGEX, message = ErrorMessage.INVALID_FORMAT_PHONE)
        String phone,

        @Schema(description = "Loại đặt lịch (AT_CLINIC, AT_HOME)", example = "AT_CLINIC")
        @NotNull(message = ErrorMessage.NOT_BLANK_FIELD)
        BookingType bookingType,

        @Schema(description = "Địa chỉ nếu đặt lịch tại nhà", example = "Số 1, đường Nhổn, Hà Nội")
        String homeAddress,

        @Schema(description = "Số lượng thú cưng", example = "1")
        @NotNull(message = ErrorMessage.NOT_BLANK_FIELD)
        Integer petQuantity,

        @Schema(description = "Loại thú cưng", example = "Chó")
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        String petType,

        @Schema(description = "Tình trạng sức khỏe thú cưng", example = "Thú cưng bị sốt nhẹ")
        @Size(max = CommonConstant.CONDITION_LENGTH, message = ErrorMessage.INVALID_FORMAT_CONDITION)
        String petCondition,

        @Schema(description = "Ngày khám", example = "2026-07-05")
        @NotNull(message = ErrorMessage.NOT_BLANK_FIELD)
        LocalDate appointmentDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(description = "Giờ khám", example = "09:30")
        @NotNull(message = ErrorMessage.NOT_BLANK_FIELD)
        LocalTime appointmentTime,

        @Schema(description = "Danh sách dịch vụ", example = "[\"Tiêm phòng\", \"Phẫu thuật\"]")
        @NotNull(message = ErrorMessage.NOT_BLANK_FIELD)
        @Size(min = 1)
        List<Long> serviceIds
) {
}
