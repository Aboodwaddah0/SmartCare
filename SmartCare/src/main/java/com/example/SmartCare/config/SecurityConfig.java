package com.example.SmartCare.config;

import com.example.SmartCare.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/appointments/*/complete").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/appointments/*").hasRole("PATIENT")
                        .requestMatchers("/api/appointments/doctor/**").hasAnyRole("DOCTOR","ADMIN")
                        .requestMatchers("/api/appointments/patient/**").hasAnyRole("PATIENT","ADMIN")
                        .requestMatchers("/api/appointments/available-slots/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/patients").hasRole("ADMIN")
                        .requestMatchers("/api/patients/search/**").hasAnyRole("ADMIN","DOCTOR","PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/*").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/*").hasRole("ADMIN")
                        .requestMatchers("/api/patients/*").hasAnyRole("ADMIN","DOCTOR","PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/patients").hasAnyRole("ADMIN","DOCTOR")

                        .requestMatchers(HttpMethod.POST, "/api/doctors").hasRole("ADMIN")
                        .requestMatchers("/api/doctors/specialty").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/*").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/doctors/*").hasRole("ADMIN")
                        .requestMatchers("/api/doctors/*").hasAnyRole("ADMIN","DOCTOR","PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/doctors").hasAnyRole("ADMIN","DOCTOR")

                        .requestMatchers(HttpMethod.POST, "/api/prescriptions").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/prescriptions/*").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/prescriptions/*").hasRole("DOCTOR")
                        .requestMatchers("/api/prescriptions/patient/**").hasAnyRole("PATIENT","DOCTOR")

                        .requestMatchers(HttpMethod.POST, "/api/medical-history").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-history/*").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-history/*").hasRole("DOCTOR")
                        .requestMatchers("/api/medical-history/patient/**").hasAnyRole("PATIENT","DOCTOR","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/schedule/*").hasAnyRole("DOCTOR","ADMIN")
                        .requestMatchers("/api/schedule/**").hasAnyRole("DOCTOR","ADMIN")

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
                        })
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
