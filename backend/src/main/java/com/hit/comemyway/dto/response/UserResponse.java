package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Thông tin chi tiết của người dùng trả về cho Client")public record UserResponse(@Schema(description="Mã định danh duy nhất",example="1")Long id,

@Schema(description="Tên đăng nhập hệ thống",example="khanhtom")String username,

@Schema(description="Địa chỉ email cá nhân",example="khanhn.nv@gmail.com")String email){}
