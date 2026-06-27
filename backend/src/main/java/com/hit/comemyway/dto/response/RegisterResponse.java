package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponse(@Schema(description="Tên người dùng sau khi đăng ký thành công",example="khanhtom110")String username,

@Schema(description="Địa chỉ email đã đăng ký",example="khanh.nv@gmail.com")String email){public static RegisterResponse from(User user){return new RegisterResponse(user.getUsername(),user.getEmail());}}
