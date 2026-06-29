package com.hit.comemyway.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultSuggestClinicsRequest {
  // @formatter:off
        @Parameter(description = "Vĩ độ của người dùng")
        @DecimalMin("-90.0") @DecimalMax("90.0")
        private Double latitude;

        @Parameter(description = "Kinh độ của người dùng")
        @DecimalMin("-180.0") @DecimalMax("180.0")
        private Double longitude;

        @Parameter(description = "Số lượng phòng khám tối đa trên một trang gợi ý")
        @Min(1) @Max(8)
        private Integer limit=5;
}
