package com.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventoResponseDto {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Long professorId;
    private String professorNome;
}
