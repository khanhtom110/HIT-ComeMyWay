package com.hit.comemyway;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.hit.comemyway.config.SecurityConfig;
import com.hit.comemyway.entity.Role;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.repository.InvalidatedRepository;
import com.hit.comemyway.security.CustomUserDetailService;
import com.hit.comemyway.security.CustomUserDetails;
import com.hit.comemyway.security.JwtAuthenticationFilter;
import com.hit.comemyway.security.JwtService;
import com.hit.comemyway.security.RateLimitingFilter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import tools.jackson.databind.json.JsonMapper;

class ComemywayApplicationTests {
  private static final String SECRET = "test-only-signing-secret-at-least-32-characters";
  private final InvalidatedRepository invalidated = mock(InvalidatedRepository.class);
  private final CustomUserDetailService users = mock(CustomUserDetailService.class);
  private final JwtService jwt = new JwtService(invalidated);
  private final User user = User.builder().id(1L).username("test-user").role(Role.USER).build();
  private AnnotationConfigWebApplicationContext context;
  private Filter security;

  @BeforeEach
  void setUpSecurityChain() throws Exception {
    SecurityContextHolder.clearContext();
    ReflectionTestUtils.setField(jwt, "secretKey", SECRET);
    ReflectionTestUtils.setField(jwt, "jwtAccessExpiration", 60000L);
    ReflectionTestUtils.setField(jwt, "jwtRefreshExpiration", 120000L);
    when(users.loadUserByUsername("test-user")).thenReturn(new CustomUserDetails(user));
    var filter = new JwtAuthenticationFilter(jwt, users, JsonMapper.builder().build());
    ReflectionTestUtils.setField(filter, "publicEndpoints",
        new String[] {"/api/v1/auth/**", "/api/v1/public/**"});
    ReflectionTestUtils.setField(filter, "swaggerEndpoints", new String[] {"/v3/api-docs/**"});
    var rateLimit = mock(RateLimitingFilter.class);
    doAnswer(invocation -> {
      FilterChain chain = invocation.getArgument(2);
      chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
      return null;
    }).when(rateLimit).doFilter(any(), any(), any());
    context = new AnnotationConfigWebApplicationContext();
    context.setServletContext(new MockServletContext());
    TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
        "security.public-endpoints=/api/v1/auth/**,/api/v1/public/**",
        "security.swagger-endpoints=/v3/api-docs/**", "security.user-endpoints=/api/v1/user/**",
        "security.clinic-endpoints=/api/v1/clinic/**", "security.admin-endpoints=/api/v1/admin/**");
    context.addBeanFactoryPostProcessor(factory -> {
      factory.registerSingleton("customUserDetailService", users);
      factory.registerSingleton("jwtAuthenticationFilter", filter);
      factory.registerSingleton("rateLimitingFilter", rateLimit);
    });
    context.register(SecurityConfig.class);
    context.refresh();
    security = context.getBean("springSecurityFilterChain", Filter.class);
  }

  @AfterEach
  void closeContext() {
    SecurityContextHolder.clearContext();
    if (context != null)
      context.close();
  }

  private MockHttpServletResponse request(String path, String authorization) throws Exception {
    var request = new MockHttpServletRequest("GET", path);
    request.setServletPath(path);
    if (authorization != null)
      request.addHeader("Authorization", authorization);
    var response = new MockHttpServletResponse();
    security.doFilter(request, response, (req, res) -> res.getWriter().write("controller reached"));
    return response;
  }

  private void assertUnauthorized(String token) throws Exception {
    var response = request("/api/v1/user/profile", token == null ? null : "Bearer " + token);
    assertEquals(401, response.getStatus());
    var body = JsonMapper.builder().build().readTree(response.getContentAsString());
    assertEquals(401, body.get("statusCode").asInt());
    assertEquals(token == null ? "A valid access token is required."
        : "The access token is invalid or has expired.", body.get("message").asText());
    assertTrue(body.get("data").isNull());
    assertNotNull(body.get("timestamp"));
    assertEquals("Bearer", response.getHeader("WWW-Authenticate"));
    assertFalse(response.getContentAsString().contains("controller reached"));
  }

  @Test
  void missingMalformedAndEmptyTokensReturn401() throws Exception {
    assertUnauthorized(null);
    assertUnauthorized("invalid-test-token");
    assertUnauthorized("");
    assertEquals(401, request("/api/v1/user/profile", "Basic invalid").getStatus());
  }

  @Test
  void expiredTokenReturns401() throws Exception {
    ReflectionTestUtils.setField(jwt, "jwtAccessExpiration", -1000L);
    assertUnauthorized(jwt.generateToken(user, false));
  }

  @Test
  void wrongSignatureReturns401() throws Exception {
    ReflectionTestUtils.setField(jwt, "secretKey", "different-test-secret-at-least-32-characters");
    String token = jwt.generateToken(user, false);
    ReflectionTestUtils.setField(jwt, "secretKey", SECRET);
    assertUnauthorized(token);
  }

  @Test
  void refreshAndRevokedTokensReturn401() throws Exception {
    assertUnauthorized(jwt.generateToken(user, true));
    when(invalidated.existsById(anyString())).thenReturn(true);
    assertUnauthorized(jwt.generateToken(user, false));
  }

  @Test
  void missingRequiredClaimsReturn401() throws Exception {
    var claims = new JWTClaimsSet.Builder().subject("test-user").build();
    var token = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
    token.sign(new MACSigner(SECRET));
    assertUnauthorized(token.serialize());
  }

  @Test
  void validTokenWorksButWrongRoleReturns403() throws Exception {
    String authorization = "Bearer " + jwt.generateToken(user, false);
    assertEquals("controller reached",
        request("/api/v1/user/profile", authorization).getContentAsString());
    var denied = request("/api/v1/admin/test", authorization);
    assertEquals(403, denied.getStatus());
    assertEquals(403, JsonMapper.builder().build().readTree(denied.getContentAsString())
        .get("statusCode").asInt());
    assertEquals("You do not have permission to access this resource.",
        JsonMapper.builder().build().readTree(denied.getContentAsString()).get("message").asText());
  }

  @Test
  void publicAndRefreshRoutesIgnoreExpiredAuthorizationHeader() throws Exception {
    ReflectionTestUtils.setField(jwt, "jwtAccessExpiration", -1000L);
    String authorization = "Bearer " + jwt.generateToken(user, false);
    for (String path : new String[] {"/api/v1/auth/refresh", "/api/v1/auth/login",
        "/api/v1/public/clinic-posts", "/v3/api-docs"}) {
      assertEquals("controller reached", request(path, authorization).getContentAsString());
    }
    verifyNoInteractions(users, invalidated);
  }

  @Test
  void infrastructureFailureIsNotReportedAsInvalidToken() {
    when(users.loadUserByUsername(anyString()))
        .thenThrow(new DataAccessResourceFailureException("database unavailable"));
    assertThrows(DataAccessResourceFailureException.class,
        () -> request("/api/v1/user/profile", "Bearer " + jwt.generateToken(user, false)));
  }

  @Test
  void refreshServiceRejectsInvalidTokensAndRotatesValidRefreshToken() {
    var repository = mock(com.hit.comemyway.repository.UserRepository.class);
    when(repository.findByUsername("test-user")).thenReturn(java.util.Optional.of(user));
    var service = new com.hit.comemyway.service.AuthService(repository, null, jwt, null,
        invalidated, null, null, null);
    var malformed = new com.hit.comemyway.dto.request.RefreshTokenRequest("invalid-token");
    assertEquals(401, assertThrows(com.hit.comemyway.exception.extended.AppException.class,
        () -> service.refreshToken(malformed)).getErrorCode());
    var access =
        new com.hit.comemyway.dto.request.RefreshTokenRequest(jwt.generateToken(user, false));
    assertEquals(401, assertThrows(com.hit.comemyway.exception.extended.AppException.class,
        () -> service.refreshToken(access)).getErrorCode());
    ReflectionTestUtils.setField(jwt, "jwtRefreshExpiration", -1000L);
    var expired =
        new com.hit.comemyway.dto.request.RefreshTokenRequest(jwt.generateToken(user, true));
    assertEquals(401, assertThrows(com.hit.comemyway.exception.extended.AppException.class,
        () -> service.refreshToken(expired)).getErrorCode());
    ReflectionTestUtils.setField(jwt, "jwtRefreshExpiration", 120000L);
    var valid =
        new com.hit.comemyway.dto.request.RefreshTokenRequest(jwt.generateToken(user, true));
    var refreshed = service.refreshToken(valid);
    assertTrue(jwt.isAccessToken(refreshed.accessToken()));
    assertFalse(jwt.isAccessToken(refreshed.refreshToken()));
    assertNotEquals(valid.refreshToken(), refreshed.refreshToken());
    verify(invalidated).save(any());
  }
}
