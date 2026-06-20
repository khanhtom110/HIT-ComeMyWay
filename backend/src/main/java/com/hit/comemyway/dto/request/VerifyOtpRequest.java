package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Email(message=ErrorMessage.INVALID_FORMAT_EMAIL)@Schema(description="The registered email",example="khanh.nv@example.com")String email,

@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Schema(description="OTP sent to the email",example="123456")String otp){}
