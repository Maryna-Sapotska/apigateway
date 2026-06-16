package com.innowise.apigateway.controller;

import com.innowise.apigateway.model.dto.GatewayRegisterRequest;
import com.innowise.apigateway.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService service;

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody GatewayRegisterRequest request) {
        return service.register(request)
                .thenReturn(ResponseEntity.ok("User registered"))
                .onErrorResume(error -> {
                    log.error("Registration error", error);
                    return Mono.error(error);
                });
    }
}
