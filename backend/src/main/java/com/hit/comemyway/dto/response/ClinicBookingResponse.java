package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Clinic;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ClinicBookingResponse(
//@formatter:off
        @Schema(description = "ID của phòng khám", example = "1")
        Long id,

        @Schema(description = "Ảnh đại diện của phòng khám", example = "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg")
        String thumbnailUrl,

        @Schema(description = "Tên phòng khám", example = "Phòng khám thú y FunVet")
        String name,

        @Schema(description = "Trạng thái hoạt động", example = "true")
        Boolean isOperating,

        @Schema(description = "Điểm đánh giá trung bình", example = "4.8")
        Double rating,

        @Schema(description = "Danh sách các dịch vụ", example = "[\"Tiêm phòng\", \"Phẫu thuật\"]")
        List<String> services
) {
    public static ClinicBookingResponse from(Clinic clinic, Boolean isOperating){
        return new ClinicBookingResponse(
        clinic.getId(),
        clinic.getThumbnailUrl(),
        clinic.getName(),
        isOperating,
        clinic.getRating(),
        clinic.getServices().stream()
                .map(service -> service.getName())
                .toList()
        );
    }
}
