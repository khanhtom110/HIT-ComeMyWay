package com.hit.comemyway.service;

import com.hit.comemyway.constant.RoleConstant;
import com.hit.comemyway.dto.request.LoginRequest;
import com.hit.comemyway.dto.request.LogoutRequest;
import com.hit.comemyway.dto.request.RefreshTokenRequest;
import com.hit.comemyway.dto.request.RegisterRequest;
import com.hit.comemyway.dto.response.LoginResponse;
import com.hit.comemyway.entity.RefreshToken;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.exception.extended.DuplicateResourceException;
import com.hit.comemyway.exception.extended.ResourceNotFoundException;
import com.hit.comemyway.repository.RefreshTokenRepository;
import com.hit.comemyway.repository.UserRepository;
import com.hit.comemyway.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {

        // kiểm tra Username & Password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.username()));

        String token = jwtService.generateToken(user);

        RefreshToken refreshToken = createRefreshToken(user);

        return new LoginResponse(token, refreshToken.getToken());
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new AppException(400, "Mật khẩu xác nhận không trùng khớp");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("User", "username", request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email", request.email());
        }

        String password = passwordEncoder.encode(request.password());

        User user = User.builder()
                .username(request.username())
                .password(password)
                .email(request.email())
                .role(RoleConstant.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        //Xoa refresh token cu
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        //Tao token moi
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(604800000)) // Han 7 ngay
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public LoginResponse refreshAccessToken(RefreshTokenRequest request) {
        return refreshTokenRepository.findByToken(request.refreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    //Tao access token
                    String newAccessToken = jwtService.generateToken(user);
                    return new LoginResponse(newAccessToken, request.refreshToken());
                })
                .orElseThrow(() -> new AppException(404, "Refresh token not found"));
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new AppException(400, "Refresh token is expired, Login against please");
        }
        return token;
    }

    @Transactional
    public void logout(LogoutRequest request) {
        refreshTokenRepository.findByToken(request.refreshToken())
                .ifPresent(refreshTokenRepository::delete);
    }
}
