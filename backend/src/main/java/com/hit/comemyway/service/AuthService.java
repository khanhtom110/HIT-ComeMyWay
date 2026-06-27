package com.hit.comemyway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.*;
import com.hit.comemyway.dto.response.LoginResponse;
import com.hit.comemyway.dto.response.RegisterResponse;
import com.hit.comemyway.entity.Role;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.InvalidatedRepository;
import com.hit.comemyway.repository.UserRepository;
import com.hit.comemyway.security.CustomUserDetails;
import com.hit.comemyway.security.JwtService;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final InvalidatedRepository invalidatedRepository;

  private final ForgotPasswordService forgotPasswordService;
  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, String> redisTemplate;


  public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new AppException(401, ErrorMessage.Auth.INVALID_CREDENTIALS));

    boolean isPasswordMatch = passwordEncoder.matches(request.password(), user.getPassword());

    if (!isPasswordMatch) {
      throw new AppException(401, ErrorMessage.Auth.INVALID_CREDENTIALS);
    }

    String accessToken = jwtService.generateToken(user, false);
    String refreshToken = jwtService.generateToken(user, true);

    return new LoginResponse(accessToken, refreshToken);
  }

  @Transactional
  public void initRegister(RegisterRequest request) {
    if (!request.password().equals(request.confirmPassword())) {
      throw new AppException(400, ErrorMessage.PASSWORD_MISMATCH);
    }

    if (userRepository.existsByUsername(request.username())) {
      throw new AppException(400, ErrorMessage.User.USERNAME_EXISTED);
    }

    if (userRepository.existsByEmail(request.email())) {
      throw new AppException(400, ErrorMessage.User.EMAIL_EXISTED);
    }

    String otp = String.format("%06d", new Random().nextInt(1000000));

    try {
      String userInfor = objectMapper.writeValueAsString(request);
      redisTemplate.opsForValue().set("REGISTRATION_DATA:" + request.email(), userInfor, 5,
          TimeUnit.MINUTES);
      redisTemplate.opsForValue().set("REGISTRATION_OTP:" + request.email(), otp, 5,
          TimeUnit.MINUTES);
    } catch (JsonProcessingException e) {
      throw new AppException(500, ErrorMessage.EXCEPTION_GENERAL);
    }

    forgotPasswordService.sendOtpEmail(request.email(), otp);
  }

  @Transactional
  public RegisterResponse verifyRegister(VerifyOtpRequest request) {

    String registerOTP = redisTemplate.opsForValue().get("REGISTRATION_OTP:" + request.email());
    if (registerOTP == null || !registerOTP.equals(request.otp())) {
      throw new AppException(400, ErrorMessage.Auth.INVALID_OTP);
    }

    String userInfor = redisTemplate.opsForValue().get("REGISTRATION_DATA:" + request.email());
    if (userInfor == null) {
      throw new AppException(400, ErrorMessage.Auth.SESSION_EXPIRED);
    }

    try {
      RegisterRequest registerRequest = objectMapper.readValue(userInfor, RegisterRequest.class);

      String password = passwordEncoder.encode(registerRequest.password());
      User user = User.builder().username(registerRequest.username()).password(password)
          .email(registerRequest.email()).role(Role.USER).build();

      redisTemplate.delete("REGISTRATION_OTP:" + request.email());
      redisTemplate.delete("REGISTRATION_DATA:" + request.email());

      User savedUser = userRepository.save(user);
      return RegisterResponse.from(savedUser);
    } catch (JsonProcessingException e) {
      throw new AppException(500, ErrorMessage.EXCEPTION_GENERAL);
    }
  }

  @Transactional
  public void logout(LogoutRequest request) {
    try {
      String token = request.refreshToken();

      if (jwtService.isAccessToken(token)) {
        throw new AppException(400, ErrorMessage.Auth.INVALID_LOGOUT_TOKEN);
      }

      SignedJWT signedJWT = SignedJWT.parse(token);
      String jti = signedJWT.getJWTClaimsSet().getJWTID();

      if (invalidatedRepository.existsById(jti)) {
        throw new AppException(400, ErrorMessage.Auth.TOKEN_ALREADY_INVALIDATED);
      }

      jwtService.invalidateToken(token);
    } catch (ParseException e) {
      throw new AppException(400, ErrorMessage.Auth.MALFORMED_TOKEN);
    } catch (AppException e) {
      throw e;
    } catch (Exception e) {
      throw new AppException(500, ErrorMessage.EXCEPTION_GENERAL);
    }
  }

  @Transactional
  public LoginResponse refreshToken(RefreshTokenRequest request) {
    String token = request.refreshToken();

    String username = jwtService.extractUsername(token);

    // Tim user trong db
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    CustomUserDetails userDetails = new CustomUserDetails(user);

    // Neu token khong hop le
    if (!jwtService.isTokenValid(token, userDetails) || jwtService.isAccessToken(token)) {
      throw new AppException(400, ErrorMessage.Auth.INVALID_REFRESH_TOKEN);
    }

    // Dua token cu vao InvalidatedToken
    jwtService.invalidateToken(token);

    String newAccessToken = jwtService.generateToken(user, false);
    String newRefreshToken = jwtService.generateToken(user, true);

    return new LoginResponse(newAccessToken, newRefreshToken);
  }
}
