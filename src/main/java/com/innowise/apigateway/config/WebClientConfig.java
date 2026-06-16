package com.innowise.apigateway.config;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf().disable()
//                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/api/auth/register", "/api/auth/login").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .build();
//    }
//}
