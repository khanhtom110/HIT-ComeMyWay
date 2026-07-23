package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserResponse(
// @formatter:off
        @Schema(description = "Mã định danh duy nhất", example = "1")
        Long id,

        @Schema(description = "Địa chỉ email cá nhân", example = "khanhn.nv@gmail.com")
        String email,

        @Schema(description = "Ảnh đại diện", example = "https://example.com/user.jpg")
        String avatar,

        @Schema(description = "Sở thích")
        String hobby
) {
    public static UpdateUserResponse from(User user) {
        return new UpdateUserResponse(
                user.getId(),
                user.getEmail(),
                user.getAvatar(),
                user.getHobby()
        );
    }
}
