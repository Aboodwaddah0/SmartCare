package com.example.mainservice.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GatewayAuthenticationFilter gatewayAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v2/doctors").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/api/v2/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                .requestMatchers("/api/v2/patients").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/api/v2/patients/search/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                .requestMatchers("/api/v2/patients/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                .anyRequest().authenticated()
            )
            .addFilterBefore(gatewayAuthenticationFilter, AuthorizationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GatewayAuthenticationFilter gatewayAuthenticationFilter() {
        return new GatewayAuthenticationFilter();
    }

    public static class GatewayAuthenticationFilter implements Filter {
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String roles = request.getHeader("X-User-Roles");
            String username = request.getHeader("X-User-Username");

            if (roles != null && username != null) {
                List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(roles)
                );
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(username, null, authorities));
            }
            filterChain.doFilter(request, servletResponse);
        }
    }
}
