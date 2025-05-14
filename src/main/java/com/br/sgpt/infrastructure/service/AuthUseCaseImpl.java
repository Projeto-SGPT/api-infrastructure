package com.br.sgpt.infrastructure.service;

import com.br.sgpt.domain.entity.User;
import com.br.sgpt.domain.repository.UserRepository;
import com.br.sgpt.domain.services.AuthService;
import com.br.sgpt.domain.usecases.auth.AuthUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthUseCaseImpl implements AuthUseCase {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public AuthUseCaseImpl(UserRepository userRepository, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String username, String password) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        return this.authService.generateToken(user);
    }

    @Override
    public void register(String username, String password, String email, String role) {
        String encoded = this.passwordEncoder.encode(password);

        this.userRepository.save(new User(UUID.randomUUID(), username, encoded, email, role));
    }
}