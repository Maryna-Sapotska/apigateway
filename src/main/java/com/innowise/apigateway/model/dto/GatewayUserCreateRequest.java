package com.innowise.apigateway.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GatewayUserCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}
