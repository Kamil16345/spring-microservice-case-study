package com.betacom.demo.dto.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String login;
    private String password;
}
