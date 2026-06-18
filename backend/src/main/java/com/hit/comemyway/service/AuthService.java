package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.LoginRequest;
import com.hit.comemyway.dto.request.LogoutRequest;
import com.hit.comemyway.dto.request.RefreshTokenRequest;
import com.hit.comemyway.dto.request.RegisterRequest;
import com.hit.comemyway.dto.response.LoginResponse;
import com.hit.comemyway.entity.Role;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.exception.extended.DuplicateResourceException;
import com.hit.comemyway.exception.extended.ResourceNotFoundException;
import com.hit.comemyway.repository.InvalidatedRepository;
import com.hit.comemyway.repository.UserRepository;
import com.hit.comemyway.security.CustomUserDetails;
import com.hit.comemyway.security.JwtService;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final InvalidatedRepository invalidatedRepository;


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
  public void register(RegisterRequest request) {
    if (!request.password().equals(request.confirmPassword())) {
      throw new AppException(400, ErrorMessage.PASSWORD_MISMATCH);
    }

    if (userRepository.existsByUsername(request.username())) {
      throw new AppException(400, ErrorMessage.User.USERNAME_EXISTED);
    }

    if (userRepository.existsByEmail(request.email())) {
      throw new AppException(400, ErrorMessage.User.EMAIL_EXISTED);
    }

    String password = passwordEncoder.encode(request.password());

    User user = User.builder().username(request.username()).password(password)
        .email(request.email()).role(Role.USER).build();

    userRepository.save(user);
  }

  @Transactional
  public void logout(LogoutRequest request) throws ParseException {
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
        .orElseThrow(() -> new AppException(400, ErrorMessage.User.USER_NOT_EXISTED));

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
