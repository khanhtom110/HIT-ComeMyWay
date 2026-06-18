package com.hit.comemyway.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank(message = "Refresh Token không được để trống")
        String refreshToken
) {
}
