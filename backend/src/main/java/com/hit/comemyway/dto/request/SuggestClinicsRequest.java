package com.hit.comemyway.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
public class SuggestClinicsRequest {
  // @formatter:off
        @Parameter(description="Từ khóa")
        private String keyword;

        @Parameter(description="Số lượng gợi ý (tối đa 10)")
        @Min(1) @Max(10)
        private Integer limit = 5;
}
