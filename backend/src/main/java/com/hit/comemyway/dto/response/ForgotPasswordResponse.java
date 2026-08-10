package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record ForgotPasswordResponse(
//@formatter:off
        @Schema(description = "Vai trò", example = "ADMIN")
        Role role
) {}
