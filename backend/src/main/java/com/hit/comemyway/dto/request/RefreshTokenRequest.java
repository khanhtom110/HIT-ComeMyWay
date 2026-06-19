package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank(message=ErrorMessage.NOT_BLANK_FIELD)String refreshToken){}
