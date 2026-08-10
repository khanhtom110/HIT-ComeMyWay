package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindFriendResponse(
// @formatter:off
        @Schema(description = "ID của người bạn", example = "1")
        Long friendId,

        @Schema(description = "Tên của người bạn", example = "maihunw")
        String username,

        @Schema(description = "Avatar của người bạn", example = "https://example.com/user.jpg")
        String avatar
) {
    public static FindFriendResponse from(User user) {
        return new FindFriendResponse(
                user.getId(),
                user.getUsername(),
                user.getAvatar()
        );
    }
}