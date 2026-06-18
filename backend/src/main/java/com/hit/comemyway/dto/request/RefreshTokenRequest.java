package com.hit.comemyway.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh Token không được để trống")
        String refreshToken
) {
}
