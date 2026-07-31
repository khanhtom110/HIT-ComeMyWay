package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.List;

public record ClinicSearchResponse(
// @formatter:off
        @Schema(description = "ID phòng khám", example = "1")
        Long id,

        @Schema(description = "URL ảnh đại diện", example = "https://example.com/clinic.jpg")
        String thumbnailUrl,

        @Schema(description = "Tên phòng khám", example = "Phòng khám thú y Xanh")
        String name,

        @Schema(description = "Trạng thái hoạt động", example = "true")
        Boolean isOperating,

        @Schema(description = "Đánh giá trung bình", example = "4.5")
        Double rating,

        @Schema(description = "Khoảng cách đến người dùng (km)", example = "2.5")
        Double distance,

        @Schema(description = "Địa chỉ chi tiết", example = "83 Giải Phóng, P. Đồng Tâm")
        String address,

        @Schema(description = "Giờ mở cửa", example = "08:00:00")
        LocalTime openTime,

        @Schema(description = "Giờ đóng cửa", example = "21:00:00")
        LocalTime closeTime,

        @Schema(description = "Danh sách dịch vụ cung cấp", example = "[\"Tiêm phòng\", \"Phẫu thuật\"]")
        List<String> services) {
}
