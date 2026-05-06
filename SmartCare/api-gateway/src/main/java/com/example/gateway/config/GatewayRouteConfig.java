package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("main-service", r -> r
						.path("/api/v2/users/**", "/api/v2/doctors/**", "/api/v2/patients/**")
						.uri("lb://main-service"))
				.route("appointment-service", r -> r
						.path("/api/v2/appointments/**", "/api/v2/slots/**", "/api/v2/schedule/**")
						.uri("lb://appointment-service"))
				.route("medical-record-service", r -> r
						.path("/api/v2/prescriptions/**", "/api/v2/medical-history/**")
						.uri("lb://medical-record-service"))
				.route("chatbot-service", r -> r
						.path("/api/v2/chat/**")
						.uri("lb://chatbot-service"))
				.build();
	}
}
