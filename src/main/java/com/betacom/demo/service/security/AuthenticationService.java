package com.betacom.demo.service.security;

import com.betacom.demo.dto.security.AuthenticationRequest;
import com.betacom.demo.dto.security.AuthenticationResponse;
import com.betacom.demo.dto.security.RegisterRequest;
import com.betacom.demo.exception.UserAlreadyExistsException;
import com.betacom.demo.exception.UserDoesNotExistException;
import com.betacom.demo.model.security.User;
import com.betacom.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        User userByLogin = userRepository.findUserByLogin(request.getLogin());
        if (userByLogin != null) {
            throw new UserAlreadyExistsException(String.format("User with email %s already exists.", request.getLogin()));
        }
        User user = User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return String.format("User %s created successfully! User's ID: %s", request.getLogin(), user.getId());
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
            );
        } catch (Exception exception) {
            throw new UserDoesNotExistException("Could not find user with given credentials. Validate and try again.");
        }
        var user = userRepository.findUserByLogin(request.getLogin());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
