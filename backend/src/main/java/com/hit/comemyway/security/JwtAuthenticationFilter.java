package com.hit.comemyway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.exception.extended.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final CustomUserDetailService userDetailService;
  private final ObjectMapper objectMapper;

  @Value("${security.public-endpoints}")
  private String[] publicEndpoints;

  @Value("${security.swagger-endpoints}")
  private String[] swaggerEndpoints;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI().substring(request.getContextPath().length());
    AntPathMatcher matcher = new AntPathMatcher();
    return java.util.stream.Stream
        .concat(java.util.Arrays.stream(publicEndpoints), java.util.Arrays.stream(swaggerEndpoints))
        .anyMatch(pattern -> matcher.match(pattern, path));
  }

  public void writeSecurityError(HttpServletResponse response, int status, String message)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    if (status == 401) {
      response.setHeader("WWW-Authenticate", "Bearer");
    }
    response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(status, message)));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!authHeader.regionMatches(true, 0, "Bearer ", 0, 7) || authHeader.substring(7).isBlank()) {
      SecurityContextHolder.clearContext();
      writeSecurityError(response, 401, "Token đã hết hạn hoặc không hợp lệ");
      return;
    }
    final String token = authHeader.substring(7).trim();

    try {
      String username = jwtService.extractUsername(token);

      if (username != null && !username.isBlank()) {
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        if (jwtService.isTokenValid(token, userDetails) && jwtService.isAccessToken(token)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
          throw new AppException(401, "Token đã hết hạn hoặc không hợp lệ");
        }
      } else {
        throw new AppException(401, "Token đã hết hạn hoặc không hợp lệ");
      }
    } catch (AppException e) {
      if (e.getErrorCode() != 401 && e.getErrorCode() != 404) {
        throw e;
      }
      SecurityContextHolder.clearContext();
      writeSecurityError(response, 401, "Token đã hết hạn hoặc không hợp lệ");
      return;
    } catch (UsernameNotFoundException e) {
      SecurityContextHolder.clearContext();
      writeSecurityError(response, 401, "Token đã hết hạn hoặc không hợp lệ");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
