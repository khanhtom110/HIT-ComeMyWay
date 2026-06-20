package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.dto.request.ResetPasswordRequest;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ForgotPasswordService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public String sendOtpForgotPassword(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new AppException(400, ErrorMessage.User.USER_NOT_EXISTED);
        }

        String otp = String.format("%06d", new Random().nextInt(1000000));

        redisTemplate.opsForValue().set("OTP:" + email, otp, 5, TimeUnit.MINUTES);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("maihunw@gmail.com");
            message.setTo(email);
            message.setSubject("Khôi phục mật khẩu");
            message.setText("Mã OTP của bạn là: " + otp);

            mailSender.send(message);
        } catch (Exception e) {
            throw new AppException(500, ErrorMessage.Auth.SEND_MAIL_FAIL);
        }

        System.out.println("Mã OTP của " + email + " : " + otp);
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

    public boolean resetPassword(ResetPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new AppException(400, ErrorMessage.PASSWORD_MISMATCH);
        }

        String email = redisTemplate.opsForValue().get("RESET_TOKEN:" + request.token());
        if (email == null) {
            throw new AppException(400, ErrorMessage.Auth.RESET_SESSION_EXPIRED);
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        redisTemplate.delete("RESET_TOKEN:" + request.token());

        return true;
    }
}