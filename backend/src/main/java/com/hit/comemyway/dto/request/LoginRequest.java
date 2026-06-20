package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Schema(description="Tên đăng nhập",example="customer")String username,


@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)@Schema(description="Mật khẩu",example="Password123!")String password){}
