package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Pattern(regexp="^[a-zA-Z0-9_]{4,120}$",message=ErrorMessage.INVALID_FORMAT_USERNAME)@Schema(description="Tên đăng nhập",example="customer")String username,

@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,120}$",message=ErrorMessage.INVALID_FORMAT_PASSWORD)@Schema(description="Mật khẩu",example="Password123!")String password,

@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Schema(description="Xác nhận mật khẩu",example="Password123!")String confirmPassword,

@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Email(message=ErrorMessage.INVALID_FORMAT_EMAIL)@Schema(description="Email",example="customer@gmail.com")String email){}
