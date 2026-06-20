package com.hit.comemyway.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

  // Map lưu trữ cho từng địa chỉ IP
  private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

  // Tối đa 5 request, nạp lại 5 request mỗi 1 phút
  private Bucket createNewBucket() {
    Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
    return Bucket.builder().addLimit(limit).build();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // Chỉ áp dụng giới hạn cho API login và register
    String requestURI = request.getRequestURI();
    if (requestURI.startsWith("/api/v1/auth/login")
        || requestURI.startsWith("/api/v1/auth/register")) {

      // Lấy IP của người dùng
      String ip = request.getHeader("X-Forwarded-For");
      if (ip == null) {
        ip = request.getRemoteAddr();
      }

      // Lấy bucket của IP này, nếu chưa có thì tạo mới
      Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

      // Thử lấy 1 token từ bucket
      if (!bucket.tryConsume(1)) {
        // Nếu hết token -> Trả về lỗi 429
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.getWriter().write(
            "{\"code\": 429, \"message\": \"Too many requests. Please try again in 1 minute.\"}");
        return; // Dừng luồng chạy, chặn không cho vào Controller
      }
    }

    // Nếu hợp lệ, cho phép request đi tiếp
    filterChain.doFilter(request, response);
  }
}
