package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.base.CommonMessage;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.dto.request.LoginRequest;
import com.hit.comemyway.dto.request.LogoutRequest;
import com.hit.comemyway.dto.request.RefreshTokenRequest;
import com.hit.comemyway.dto.request.RegisterRequest;
import com.hit.comemyway.dto.response.LoginResponse;
import com.hit.comemyway.entity.RefreshToken;
import com.hit.comemyway.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1 + "/auth")
@Tag(name = "Authentication", description = "Các API Quản lý đăng nhập, đăng ký và Token")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS, response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS, null)
        );
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request){
        LoginResponse response= authService.refreshAccessToken(request);
        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS,response)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request);

        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS, null)
        );
    }
}
