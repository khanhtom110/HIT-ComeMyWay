package com.hit.comemyway.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
public class SearchClinicsRequest {
  // @formatter:off
        @Parameter(description = "Từ khóa tìm kiếm (tên, địa chỉ, hoặc dịch vụ)")
        private String keyword;

        @Parameter(description = "Vĩ độ của người dùng")
        @DecimalMin("-90.0") @DecimalMax("90.0")
        private Double latitude;

        @Parameter(description = "Kinh độ của người dùng")
        @DecimalMin("-180.0") @DecimalMax("180.0")
        private Double longitude;

        @Parameter(description = "Bán kính tìm kiếm (km)")
        @Min(1)
        private Double radius = 10.0;

        @Parameter(description = "Số lượng phòng khám tối đa trên một trang tìm kiếm")
        @Min(1) @Max(30)
        private Integer limit = 20;
}