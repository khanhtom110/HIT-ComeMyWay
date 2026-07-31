package com.hit.comemyway.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hit.comemyway.entity.Clinic;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.List;

public record CompleteClinicProfileResponse(
// @formatter:off
        @Schema(description = "ID phòng khám", example = "1")
        Long id,

        @Schema(description = "URL ảnh đại diện", example = "https://example.com/clinic.jpg")
        String thumbnailUrl,

        @Schema(description = "Tên phòng khám", example = "Phòng khám thú y Xanh")
        String name,

        @Schema(description = "Mô tả chi tiết phòng khám", example = "Phòng khám thú y uy tín hàng đầu với trang thiết bị hiện đại")
        String description,

        @Schema(description = "Số điện thoại liên hệ", example = "0383553886")
        String phone,

        @Schema(description = "Địa chỉ chi tiết", example = "83 Giải Phóng,P.Đồng Tâm")
        String address,

        @Schema(description = "Link gg map phòng khám")
        String mapLink,

        @Schema(description = "Vĩ độ của phòng khám", example = "21.0987")
        Double latitude,

        @Schema(description = "Kinh độ của phòng khám", example = "105.0987")
        Double longitude,

        @Schema(description = "Giờ mở cửa", example = "08:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime openTime,

        @Schema(description = "Giờ đóng cửa", example = "21:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime closeTime,

        @Schema(description = "DS dịch vụ", example = "[\"Tiêm phòng\", \"Phẫu thuật\"]")
        List<String> services) {
        public static CompleteClinicProfileResponse from(Clinic clinic) {
                return new CompleteClinicProfileResponse(
                        clinic.getId(),
                        clinic.getThumbnailUrl(),
                        clinic.getName(),
                        clinic.getDescription(),
                        clinic.getPhone(),
                        clinic.getAddress(),
                        clinic.getMapLink(),
                        clinic.getLatitude(),
                        clinic.getLongitude(),
                        clinic.getOpenTime(),
                        clinic.getCloseTime(),
                        clinic.getServices().stream().map(service -> service.getName()).toList()
                );
        }
}
