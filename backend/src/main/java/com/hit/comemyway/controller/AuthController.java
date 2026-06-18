package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.base.CommonMessage;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.LoginRequest;
import com.hit.comemyway.dto.request.LogoutRequest;
import com.hit.comemyway.dto.request.RefreshTokenRequest;
import com.hit.comemyway.dto.request.RegisterRequest;
import com.hit.comemyway.dto.response.LoginResponse;
import com.hit.comemyway.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
@Tag(name = "Authentication", description = "Các API Quản lý đăng nhập, đăng ký và Token")
public class AuthController {
    private final AuthService authService;

    @PostMapping(UrlConstant.Auth.LOGIN)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS, response));
    }

    @PostMapping(UrlConstant.Auth.REGISTER)
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS, null)
        );
    }

    @PostMapping(UrlConstant.Auth.REFRESH_TOKEN)
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request){
        LoginResponse response= authService.refreshToken(request);
        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS,response)
        );
    }

    @PostMapping(UrlConstant.Auth.LOGOUT)
    public ResponseEntity<ApiResponse<String>> logout(@Valid @RequestBody LogoutRequest request) throws ParseException {
        authService.logout(request);

        return ResponseEntity.ok(
                ApiResponse.ok(CommonMessage.SUCCESS, SuccessMessage.Auth.LOGOUT_SUCCESS)
        );
    }
}
