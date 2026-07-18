package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
// @formatter:off
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,120}$", message = ErrorMessage.INVALID_FORMAT_PASSWORD)
        @Schema(description = "Mật khẩu", example = "Password123!")
        String password,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Xác nhận mật khẩu", example = "Password123!")
        String confirmPassword) {}
