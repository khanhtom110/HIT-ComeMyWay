package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.ErrorMessage;
import jakarta.validation.constraints.NotNull;

public record FriendRequest(
// @formatter:off
        @NotNull(message = ErrorMessage.NOT_BLANK_FIELD)
        Long friendId
) {
}
