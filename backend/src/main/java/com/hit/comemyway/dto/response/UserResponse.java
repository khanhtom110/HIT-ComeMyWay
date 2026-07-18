package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.AccountStatus;
import com.hit.comemyway.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Thông tin chi tiết của người dùng trả về cho Client")public record UserResponse(
// @formatter:off
        @Schema(description = "Mã định danh duy nhất", example = "1")
        Long id,

        @Schema(description = "Tên đăng nhập hệ thống", example = "khanhtom")
        String username,

        @Schema(description = "Địa chỉ email cá nhân", example = "khanhn.nv@gmail.com")
        String email,

        @Schema(description = "Trạng thái tài khoản", example = " PENDING_PASSWORD_CHANGE")
        AccountStatus status){
        public static UserResponse from(User user) {
                return new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getStatus()
                );
        }
}

