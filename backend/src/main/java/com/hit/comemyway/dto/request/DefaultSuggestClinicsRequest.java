package com.hit.comemyway.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestParam;

public record DefaultSuggestClinicsRequest(
// @formatter:off
        @Parameter(description = "Vĩ độ của người dùng") @RequestParam(required = false)
        @DecimalMin("-90.0") @DecimalMax("90.0")
        Double latitude,

        @Parameter(description = "Kinh độ của người dùng") @RequestParam(required = false)
        @DecimalMin("-180.0") @DecimalMax("180.0")
        Double longitude,

        @Parameter(description = "Số lượng phòng khám tối đa trên một trang gợi ý")
        @RequestParam(defaultValue = "5")
        @Min(1) @Max(8)
        int limit
) {
}
