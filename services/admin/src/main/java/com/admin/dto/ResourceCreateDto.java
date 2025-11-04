package com.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResourceCreateDto {

    @NotBlank(message = "O name do resource é obrigatório")
    private String name;

    private String description;

    private Boolean available;

    // Getters explícitos para garantir compatibilidade
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getAvailable() {
        return available;
    }
}