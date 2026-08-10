package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateClinicAccountRequest(
// @formatter:off
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Tên đăng nhập", example = "customer")
        String username,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)
        @Schema(description = "Email", example = "customer@gmail.com")
        String email
) {}
