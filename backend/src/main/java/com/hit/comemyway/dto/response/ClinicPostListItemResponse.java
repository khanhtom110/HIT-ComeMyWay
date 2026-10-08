package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClinicPostListItemResponse(
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

        @Schema(description = "Đoạn trích bài viết", example = "Nếu thú cưng của bạn bị nôn mửa thì có khả năng bé...")
        String excerpt,

        @Schema(description = "Url ảnh đại diện bài viết", example = "https://example.com/clinic_post.jpg")
        String imageUrl){}
