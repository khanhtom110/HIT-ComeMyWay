package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record FindFriendRequest(
// @formatter:off
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Schema(description = "Link locket", example = "https://petlocket.com/add?code=bCOGC0")
        String locketLink
) {}