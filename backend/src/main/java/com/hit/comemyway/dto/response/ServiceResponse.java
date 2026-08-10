package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Service;
import io.swagger.v3.oas.annotations.media.Schema;

public record ServiceResponse(
// @formatter:off
        @Schema(description = "Mã của dịch vụ")
        Long id,

        @Schema(description = "Tên dịch vụ")
        String name
) {
    public static ServiceResponse from(Service service){
        return new ServiceResponse(
                service.getId(),
                service.getName()
        );
    }
}
