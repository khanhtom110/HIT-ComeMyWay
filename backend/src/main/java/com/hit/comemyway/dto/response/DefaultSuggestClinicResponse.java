package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DefaultSuggestClinicResponse(
// @formatter:off
        @Schema(description = "ID phòng khám", example = "1")
        Long id,

        @Schema(description = "Tên phòng khám", example = "Phòng khám thú y Xanh")
        String name,

        @Schema(description = "Địa chỉ", example = "83 Giải Phóng, P. Đồng Tâm")
        String address,

        @Schema(description = "URL ảnh đại diện", example = "https://example.com/clinic.jpg")
        String thumbnailUrl,

        @Schema(description = "Khoảng cách đến người dùng (km)", example = "2.5")
        Double distance,

        @Schema(description = "Đánh giá trung bình", example = "4.5")
        Double rating
) {}
