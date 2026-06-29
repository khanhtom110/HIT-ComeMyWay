package com.hit.comemyway.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestParam;

public record SearchClinicsRequest(
// @formatter:off
        @Parameter(description = "Từ khóa tìm kiếm (tên, địa chỉ, hoặc dịch vụ)")
        @RequestParam(required = false) String keyword,

        @Parameter(description = "Vĩ độ của người dùng") @RequestParam(required = false)
        @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,

        @Parameter(description = "Kinh độ của người dùng") @RequestParam(required = false)
        @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,

        @Parameter(description = "Bán kính tìm kiếm (km)") @RequestParam(defaultValue = "10.0")
        @Min(1) Double radius,

        @Parameter(description = "Số lượng phòng khám tối đa trên một trang tìm kiếm")
        @RequestParam(defaultValue = "20") @Min(1) @Max(30) int limit
){
}
