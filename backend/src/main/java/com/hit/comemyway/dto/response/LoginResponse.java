package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
// @formatter:off
        @Schema(description = "Token dùng để xác thực các API yêu cầu quyền")
        String accessToken,

        @Schema(description = "Mã dùng để duy trì")
        String refreshToken
) {
}
