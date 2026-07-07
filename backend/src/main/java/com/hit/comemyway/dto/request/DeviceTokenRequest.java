package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DeviceTokenRequest(
        @Schema(description = "Device token do Firebase dưới mobile cấp")
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        String deviceToken
) {}
