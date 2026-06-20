package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(@NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)@Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)String email,

@NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)String otp) {}
