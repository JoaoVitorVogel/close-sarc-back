package com.user.dto;

import lombok.Data;

@Data
public class TurmaResponseDto {
    private Long id;
    private String nome;
    private String curso;
    private String horario;
    private Long professorId;
    private String professorNome;
}
