package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.CommonConstant;
import com.hit.comemyway.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// @formatter:off
public record RejectAppointmentRequest(
        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Size(max = CommonConstant.Clinic.REJECT_REASON_LENGTH, message = ErrorMessage.Clinic.REJECT_REASON)
        String rejectReason
) {
}
