package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Size(min = 2, max = 120, message = ErrorMessage.INVALID_USERNAME_LENGTH)
        String username,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Size(min = 8, max = 120, message = ErrorMessage.INVALID_FORMAT_PASSWORD)
        String password,

        String confirmPassword,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)
        String email
) {
}
