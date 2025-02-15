package com.betacom.demo.dto.security;

import lombok.Data;

@Data
public class RegisterRequest {
    private String login;
    private String password;
}
