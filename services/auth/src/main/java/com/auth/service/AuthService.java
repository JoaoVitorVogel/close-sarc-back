package com.auth.service;

import com.auth.dto.LoginRequestDto;

import com.auth.dto.LoginResponseDto;

import com.auth.repository.AdminRepository;

import com.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService; // Nosso novo TokenService
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    public LoginResponseDto login(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        String token = tokenService.generateToken(authentication);
//autentica e da role
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        String nome = getName(request.getEmail(), role);

        return new LoginResponseDto(token, nome, role);
    }

    private String getName(String email, String role) {
        if ("ROLE_PROFESSOR".equals(role)) {
            return userRepository.findByEmail(email).map(p -> p.getNome()).orElse("");
        }
        if ("ROLE_ADMIN".equals(role)) {
            return adminRepository.findByEmail(email).map(a -> a.getNome()).orElse("");
        }
        return "";
    }
}