package com.betacom.demo.dto.security;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String login;
    private String password;
}
