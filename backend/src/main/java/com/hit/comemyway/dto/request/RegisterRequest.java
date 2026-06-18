package com.hit.comemyway.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username không được để trống")
        @Size(min = 2, max = 120, message = "Username phải từ 2-120 ký tự")
        String username,

        @NotBlank(message = "Password không được để trống")
        @Size(min = 6, max = 120, message = "Password phải từ 6-120 ký tự")
        String password,

        String confirmPassword,

        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không đúng định dạng")
        String email
) {
}
