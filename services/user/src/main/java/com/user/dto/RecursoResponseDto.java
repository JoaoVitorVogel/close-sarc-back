package com.user.dto;

import lombok.Data;

@Data
public class RecursoResponseDto {
    private Long id;
    private String nome;
    private String descricao;
    private boolean disponivel;
}
