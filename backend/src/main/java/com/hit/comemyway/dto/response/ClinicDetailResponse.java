package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Clinic;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.List;

public record ClinicDetailResponse(
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

        @Schema(description = "Mô tả chi tiết phòng khám", example = "Phòng khám thú y uy tín hàng đầu với trang thiết bị hiện đại")
        String description,


        @Schema(description = "Số điện thoại liên hệ", example = "0383553886")
        String phone,


        @Schema(description = "Địa chỉ chi tiết", example = "83Giải Phóng,P.Đồng Tâm")
        String address,

        @Schema(description = "Giờ mở cửa", example = "08:00:00")
        LocalTime openTime,

        @Schema(description = "Giờ đóng cửa", example = "21:00:00")
        LocalTime closeTime,

        @Schema(description = "DS dịch vụ", example = "[\"Tiêm phòng\", \"Phẫu thuật\"]")
        List<String> services,


        @Schema(description = "Link gg map phòng khám")
        String mapLink
        // @formatter:on
){public static ClinicDetailResponse from(Clinic clinic,boolean isOperating){return new ClinicDetailResponse(clinic.getId(),clinic.getThumbnailUrl(),clinic.getName(),isOperating,clinic.getRating(),clinic.getDescription(),clinic.getPhone(),clinic.getAddress(),clinic.getOpenTime(),clinic.getCloseTime(),clinic.getServices().stream().map(service->service.getName()).toList(),clinic.getMapLink());}}

