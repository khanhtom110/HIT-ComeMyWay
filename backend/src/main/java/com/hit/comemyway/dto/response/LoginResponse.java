package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.AccountStatus;
import com.hit.comemyway.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
// @formatter:off
        @Schema(description = "Token dùng để xác thực các API yêu cầu quyền")
        String accessToken,

        @Schema(description = "Mã dùng để duy trì")
        String refreshToken,

        @Schema(description = "Id người dùng")
        Long userId,

        @Schema(description = "Vai trò")
        Role role,

        @Schema(description = "Trạng thái tài khoản")
        AccountStatus accountStatus
) {
}
