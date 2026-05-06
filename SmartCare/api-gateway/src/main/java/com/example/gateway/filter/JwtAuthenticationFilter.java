package com.example.gateway.filter;

import com.example.gateway.config.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String path = exchange.getRequest().getPath().value();

		if (path.startsWith("/api/v2/auth/")) {
			return chain.filter(exchange);
		}

		String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return chain.filter(exchange);
		}

		String token = authHeader.substring(7);

		if (!jwtTokenProvider.isTokenValid(token)) {
			exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		try {
			Claims claims = jwtTokenProvider.parseToken(token);
			String username = claims.getSubject();
			String role = claims.get("role", String.class);
			String userId = claims.get("userId", String.class);

			ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
					.header("X-User-Id", userId)
					.header("X-User-Username", username)
					.header("X-User-Roles", "ROLE_" + role)
					.build();

			ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

			List<SimpleGrantedAuthority> authorities = List.of(
					new SimpleGrantedAuthority("ROLE_" + role)
			);

			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(username, null, authorities);

			return chain.filter(mutatedExchange)
					.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

		} catch (Exception e) {
			exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
	}
}
