package com.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResourceCreateDto {

    @NotBlank(message = "O nome do recurso é obrigatório")
    private String nome;

    private String descricao;

    private Boolean disponivel;
}