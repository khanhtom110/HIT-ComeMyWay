package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ClinicPostResponse(
// @formatter:off
        @Schema(description = "ID bài viết", example = "1")
        Long id,

        @Schema(description = "ID phòng khám", example = "1")
        Long clinicId,

        @Schema(description = "Tên phòng khám", example = "Phòng khám thú y Xanh")
        String clinicName,

        @Schema(description = "Url ảnh phòng khám", example = "https://example.com/clinic.jpg")
        String clinicAvatarUrl,

        @Schema(description = "Tiêu đề bài viết", example = "Thú cưng bị nôn mửa thì phải làm sao?")
        String title,

        @Schema(description = "Nội dung bài viết", example = "Bài viết này sẽ là cẩm nang toàn diện, giúp bạn hiểu rõ nguyên nhân khiến chó mèo nôn mửa, cách xử lý tình huống khẩn cấp tại nhà và các dấu hiệu cảnh báo cần đưa thú cưng đi cấp cứu.")
        String content,

        @Schema(description = "Url ảnh đại diện bài viết", example = "https://example.com/clinic_post.jpg")
        String imageUrl,

        @Schema(description = "Url ảnh trong bài viết", example = "[\"https://example.com/clinic_post.jpg\", \"https://example.com/clinic_post.jpg\"]")
        List<String>imageUrls){}
