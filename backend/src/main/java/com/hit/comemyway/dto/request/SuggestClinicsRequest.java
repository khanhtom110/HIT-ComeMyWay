package com.hit.comemyway.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestParam;

public record SuggestClinicsRequest(
// @formatter:off
        @Parameter(description="Từ khóa")
        @RequestParam(required=false)
        String keyword,

        @Parameter(description="Số lượng gợi ý (tối đa 10)")
        @RequestParam(defaultValue="5")
        @Min(1) @Max(10)
        int limit
){
}
