package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    // Getters explícitos para garantir compatibilidade
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}