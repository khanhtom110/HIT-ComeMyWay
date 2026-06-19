package com.hit.comemyway.config;

import com.hit.comemyway.constant.RoleConstant;
import com.hit.comemyway.security.CustomUserDetailService;
import com.hit.comemyway.security.JwtAuthenticationFilter;
import com.hit.comemyway.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final JwtAuthenticationFilter jwtAuthFilter;
    @Value("${security.public-endpoints}")
    String[] PUBLIC_END_POINT;

    @Value("${security.user-endpoints}")
    String[] USER_END_POINT;

    @Value("${security.clinic-endpoints}")
    String[] CLINIC_END_POINT;

    @Value("${security.admin-endpoints}")
    String[] ADMIN_END_POINT;

    @Value("${security.swagger-endpoints}")
    String[] OPEN_API;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // Provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_END_POINT).permitAll()
                        .requestMatchers(USER_END_POINT).hasAuthority(RoleConstant.USER)
                        .requestMatchers(CLINIC_END_POINT).hasAuthority(RoleConstant.CLINIC)
                        .requestMatchers(ADMIN_END_POINT).hasAuthority(RoleConstant.ADMIN)
                        .requestMatchers(OPEN_API).permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                // Lắp màng lọc JWT vào trước bộ lọc mặc định của Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
