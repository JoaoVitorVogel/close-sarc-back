package com.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String name;
    private String role;

    // Construtor explícito para garantir compatibilidade
    public LoginResponseDto(String token, String name, String role) {
        this.token = token;
        this.name = name;
        this.role = role;
    }
}