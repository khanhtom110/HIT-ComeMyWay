package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(@NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)String token,

@NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,120}$", message = ErrorMessage.INVALID_FORMAT_PASSWORD)String newPassword,

@NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)String confirmPassword) {}
