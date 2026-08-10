package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.CommonConstant;
import com.hit.comemyway.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PetLocketCreateRequest(
// @formatter:off
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        String imageUrl,

        @Size(max = CommonConstant.Locket.LIMIT_LENGTH_CAPTION, message = ErrorMessage.Locket.INVALID_LENGTH_CAPTION)
        String caption
) {}