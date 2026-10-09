package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ClinicAccountListResponse(
// @formatter:off
    List<ClinicStatisticsResponse> content,

    boolean hasNext,

    @Schema(description = "Cursor cho trang tiếp theo; null nếu đã hết.", nullable = true)
    Long nextCursor
) {}
