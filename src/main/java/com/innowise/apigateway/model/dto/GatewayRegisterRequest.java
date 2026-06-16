package com.innowise.apigateway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRegisterRequest {

    private String login;
    private String password;
    private String email;

    private String name;
    private String surname;
    private LocalDate birthDate;
}
