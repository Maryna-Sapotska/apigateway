package com.innowise.apigateway.service;

import com.innowise.apigateway.client.AuthClient;

import com.innowise.apigateway.exception.EmailAlreadyExistsException;
import com.innowise.apigateway.model.dto.GatewayUserCreateRequest;
import com.innowise.apigateway.model.dto.GatewayRegisterRequest;
import com.innowise.apigateway.model.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final WebClient webClient;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final AuthClient authClient;

    public Mono<TokenResponse> register(GatewayRegisterRequest request) {

        return registerInAuthService(request)
                .flatMap(tokenResponse -> processUserCreation(request, tokenResponse));
    }

    private Mono<TokenResponse> registerInAuthService(GatewayRegisterRequest request) {

        return webClient.post()
                .uri("http://auth-service:8081/api/auth/register")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleAuthError)
                .bodyToMono(TokenResponse.class);
    }

    private Mono<Throwable> handleAuthError(ClientResponse response) {

        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.error("Auth service error: {}", body);
                    return Mono.error(new RuntimeException(body));
                });
    }

    private Mono<TokenResponse> processUserCreation(
            GatewayRegisterRequest request,
            TokenResponse tokenResponse
    ) {

        Long userId = extractUserId(tokenResponse.getAccessToken());

        return createUser(request)
                .thenReturn(tokenResponse)
                .onErrorResume(error -> handleUserCreationError(error, userId));
    }

    private Mono<Void> createUser( GatewayRegisterRequest request) {

        GatewayUserCreateRequest dto = mapToUserRequest(request);

        return authClient.getAdminToken()
                .flatMap(token ->
                        webClient.post()
                                .uri("http://user-service:8080/users")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .bodyValue(dto)
                                .retrieve()
                                .onStatus(status -> status.value() == 409,
                                        this::handleUserDuplicate)
                                .onStatus(HttpStatusCode::isError,
                                        this::handleUserServiceError)
                                .bodyToMono(Void.class)
                );
    }

    private GatewayUserCreateRequest mapToUserRequest(GatewayRegisterRequest request) {

        GatewayUserCreateRequest dto = new GatewayUserCreateRequest();
        dto.setName(request.getName());
        dto.setSurname(request.getSurname());
        dto.setBirthDate(request.getBirthDate());
        dto.setEmail(request.getEmail());

        return dto;
    }

    private Mono<Throwable> handleUserDuplicate(ClientResponse response) {

        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.warn("User already exists: {}", body);
                    return Mono.error(new EmailAlreadyExistsException(body));
                });
    }

    private Mono<Throwable> handleUserServiceError(ClientResponse response) {

        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.error("User service error: {}", body);
                    return Mono.error(new RuntimeException(body));
                });
    }

    private Mono<TokenResponse> handleUserCreationError(Throwable error, Long userId) {

        if (error instanceof EmailAlreadyExistsException) {
            return Mono.error(error);
        }

        if (isDuplicateFromMessage(error)) {
            return Mono.error(new EmailAlreadyExistsException("Email already exists"));
        }

        return rollbackAuth(userId)
                .then(Mono.error(error));
    }

    private boolean isDuplicateFromMessage(Throwable error) {

        return error instanceof RuntimeException
                && error.getMessage() != null
                && error.getMessage().contains("duplicate");
    }

    private Mono<Void> rollbackAuth(Long userId) {
        return webClient.delete()
                .uri("http://auth-service:8081/api/auth/{id}", userId)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("ROLLBACK SUCCESS status={}", r.getStatusCode()))
                .doOnError(e -> log.error("2. ERROR rollback", e))
                .then()
                .doOnTerminate(() -> log.info("3. END rollback"));
    }

    private Long extractUserId(String accessToken) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        return Long.valueOf(claims.get("userId", String.class));
    }
}
