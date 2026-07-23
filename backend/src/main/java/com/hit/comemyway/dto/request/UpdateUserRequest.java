package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
// @formatter:off
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)
        @Schema(description = "Địa chỉ email cá nhân", example = "khanhn.nv@gmail.com")
        String email,

        @Schema(description = "Ảnh đại diện", example = "https://example.com/user.jpg")
        String avatar,

        @Schema(description = "Sở thích")
        String hobby
) {
}
