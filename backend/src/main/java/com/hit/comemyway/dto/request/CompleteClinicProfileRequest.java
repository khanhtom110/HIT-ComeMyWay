package com.hit.comemyway.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hit.comemyway.constant.CommonConstant;
import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

public record CompleteClinicProfileRequest(
// @formatter:off
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Tên phòng khám", example = "Phòng khám thú y Xanh")
        String name,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Địa chỉ chi tiết", example = "83 Giải Phóng, P. Đồng Tâm")
        String address,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Link gg map phòng khám")
        String mapLink,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Số điện thoại liên hệ", example = "0383553886")
        @Pattern(regexp = CommonConstant.PHONE_REGEX, message = ErrorMessage.INVALID_FORMAT_PHONE)
        String phone,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Mô tả chi tiết phòng khám", example = "Phòng khám thú y uy tín hàng đầu với trang thiết bị hiện đại")
        String description,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "URL ảnh đại diện", example = "https://example.com/clinic.jpg")
        String thumbnailUrl,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Giờ mở cửa", example = "08:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime openTime,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Giờ đóng cửa", example = "21:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime closeTime,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "DS dịch vụ", example = "[\"Tiêm phòng\", \"Phẫu thuật\"]")
        List<String> services) {}
