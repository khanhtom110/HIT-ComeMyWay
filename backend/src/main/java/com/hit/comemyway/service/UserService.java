package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.ChangePasswordRequest;
import com.hit.comemyway.dto.request.ChangeUserPasswordRequest;
import com.hit.comemyway.dto.request.CreateClinicAccountRequest;
import com.hit.comemyway.dto.request.UpdateUserRequest;
import com.hit.comemyway.dto.response.UpdateUserResponse;
import com.hit.comemyway.dto.response.UserResponse;
import com.hit.comemyway.entity.AccountStatus;
import com.hit.comemyway.entity.Role;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserService {
  private static final String CHARS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SecureRandom random;
  private final BrevoEmailService brevoEmailService;

  @Transactional
  public void updateDeviceToken(String deviceToken) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    user.setDeviceToken(deviceToken);

    userRepository.save(user);
  }

  @Transactional
  public UserResponse createClinicAccount(CreateClinicAccountRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new AppException(400, ErrorMessage.User.USERNAME_EXISTED);
    }

    if (userRepository.existsByEmail(request.email())) {
      throw new AppException(400, ErrorMessage.User.EMAIL_EXISTED);
    }

    String randomPassword = IntStream.range(0, 8)
        .mapToObj(i -> String.valueOf(CHARS.charAt(random.nextInt(CHARS.length()))))
        .collect(Collectors.joining());

    User user = User.builder().username(request.username())
        .password(passwordEncoder.encode(randomPassword)).email(request.email()).role(Role.CLINIC)
        .status(AccountStatus.PENDING_PASSWORD_CHANGE).build();

    User savedUser = userRepository.save(user);

    sendPasswordEmail(request.email(), randomPassword);

    return UserResponse.from(savedUser);
  }

  private void sendPasswordEmail(String email, String password) {
    try {
      String subject = "Mật khẩu đăng nhập App Nhịp đập thú cưng";
      String htmlContent = "<h3>Xin chào,</h3>"
          + "<p>Mật khẩu của bạn là: <strong style='font-size: 24px; color: blue;'>" + password
          + "</strong></p>" + "<p>Vui lòng không chia sẻ mật khẩu này với bất kỳ ai.</p>";

      brevoEmailService.sendEmail(email, subject, htmlContent);

    } catch (Exception e) {
      System.err
          .println("Failure when sending mail for: " + email + " - Exception: " + e.getMessage());
      throw new AppException(500, ErrorMessage.Auth.SEND_MAIL_FAIL);
    }
  }

  @Transactional
  public UserResponse changeFirstTimePassword(ChangePasswordRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    if (passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new AppException(400, ErrorMessage.Auth.PASSWORD_SAME_AS_OLD);
    }

    if (!request.password().equals(request.confirmPassword())) {
      throw new AppException(400, ErrorMessage.PASSWORD_MISMATCH);
    }

    user.setPassword(passwordEncoder.encode(request.password()));
    user.setStatus(AccountStatus.PENDING_PROFILE);

    userRepository.save(user);

    return UserResponse.from(user);
  }

  @Transactional(readOnly = true)
  public UpdateUserResponse getProfile() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    return UpdateUserResponse.from(user);
  }

  @Transactional
  public UpdateUserResponse updateUserProfile(UpdateUserRequest request, Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    user.setEmail(request.email());
    user.setAvatar(request.avatar());
    user.setHobby(request.hobby());

    return UpdateUserResponse.from(user);
  }

  @Transactional
  public void updateUserPassword(ChangeUserPasswordRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
      throw new AppException(400, ErrorMessage.Auth.INVALID_PASSWORD);
    }

    if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
      throw new AppException(400, ErrorMessage.Auth.PASSWORD_SAME_AS_OLD);
    }

    if (!request.newPassword().equals(request.confirmPassword())) {
      throw new AppException(400, ErrorMessage.PASSWORD_MISMATCH);
    }

    user.setPassword(passwordEncoder.encode(request.newPassword()));

    userRepository.save(user);
  }
}
