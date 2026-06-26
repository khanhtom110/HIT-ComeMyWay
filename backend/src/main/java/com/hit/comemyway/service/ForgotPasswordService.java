package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.dto.request.ResetPasswordRequest;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

  private final RedisTemplate<String, String> redisTemplate;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final BrevoEmailService brevoEmailService;

  public void sendOtpEmail(String email, String otp) {
    try {
      // Title and content by HTML for email
      String subject = "Mã xác nhận App Nhịp đập thú cưng";
      String htmlContent = "<h3>Xin chào,</h3>"
          + "<p>Mã OTP của bạn là: <strong style='font-size: 24px; color: blue;'>" + otp
          + "</strong></p>"
          + "<p>Mã này sẽ hết hạn sau 5 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>";

      brevoEmailService.sendOtpEmail(email, subject, htmlContent);

    } catch (Exception e) {
      System.err
          .println("Failure when sending mail for: " + email + " - Exception: " + e.getMessage());
      throw new AppException(500, ErrorMessage.Auth.SEND_MAIL_FAIL);
    }
  }

  public String sendOtpForgotPassword(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new AppException(400, ErrorMessage.User.USER_NOT_EXISTED);
    }

    String otp = String.format("%06d", new Random().nextInt(1000000));

    redisTemplate.opsForValue().set("OTP:" + email, otp, 5, TimeUnit.MINUTES);

    this.sendOtpEmail(email, otp);
    return SuccessMessage.Auth.SEND_OTP_SUCCESS;
  }

  public String verifyOtp(String email, String userInputOtp) {
    String systemOtp = redisTemplate.opsForValue().get("OTP:" + email);

    if (systemOtp == null) {
      throw new AppException(400, ErrorMessage.Auth.OTP_EXPIRED);
    }
    if (!systemOtp.equals(userInputOtp)) {
      throw new AppException(400, ErrorMessage.Auth.INVALID_OTP);
    }

    redisTemplate.delete("OTP:" + email);

    String resetToken = UUID.randomUUID().toString();
    redisTemplate.opsForValue().set("RESET_TOKEN:" + resetToken, email, 10, TimeUnit.MINUTES);

    return resetToken;
  }

  @Transactional
  public void resetPassword(ResetPasswordRequest request) {
    if (!request.newPassword().equals(request.confirmPassword())) {
      throw new AppException(400, ErrorMessage.PASSWORD_MISMATCH);
    }

    String email = redisTemplate.opsForValue().get("RESET_TOKEN:" + request.token());
    if (email == null) {
      throw new AppException(400, ErrorMessage.Auth.RESET_SESSION_EXPIRED);
    }

    var user = userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
      throw new AppException(400, ErrorMessage.Auth.PASSWORD_SAME_AS_OLD);
    }

    user.setPassword(passwordEncoder.encode(request.newPassword()));
    userRepository.save(user);

    redisTemplate.delete("RESET_TOKEN:" + request.token());

  }
}
