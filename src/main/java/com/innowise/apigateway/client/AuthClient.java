package com.innowise.apigateway.client;

import com.innowise.apigateway.model.dto.LoginRequest;
import com.innowise.apigateway.model.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthClient {

    private final WebClient webClient;

    @Value("${gateway.service-user.login}")
    private String login;

    @Value("${gateway.service-user.password}")
    private String password;

    public Mono<String> getAdminToken() {

        return webClient.post()
                .uri("http://auth-service:8081/api/auth/login")
                .bodyValue(
                        new LoginRequest(
                                login,
                                password
                        )
                )
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(TokenResponse::getAccessToken);
    }
}
