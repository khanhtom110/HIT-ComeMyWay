package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


public record ClinicStatisticsResponse(
// @formatter:off
    @Schema(description = "ID người dùng", example = "1")
    Long userId,

    @Schema(description = "ID phòng khám; null nếu tài khoản chưa có hồ sơ phòng khám", example = "1", nullable = true)
    Long clinicId,

    @Schema(description = "Tên tài khoản", example = "maihunw@gmail.com")
    String name
) {}


