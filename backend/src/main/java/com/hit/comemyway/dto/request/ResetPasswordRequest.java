package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Schema(description="Reset token received from verify-otp API",example="d8e3d641-f5b2-4d56-a97e-123456789abc")String token,

@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,120}$",message=ErrorMessage.INVALID_FORMAT_PASSWORD)@Schema(description="New password must follow complexity rules",example="Password123!")String newPassword,

@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Schema(description="Must match new password",example="Password123!")String confirmPassword){}
