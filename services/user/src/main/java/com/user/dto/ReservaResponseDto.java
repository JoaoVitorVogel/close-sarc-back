package com.user.dto;

import lombok.Data;

@Data
public class ReservaResponseDto {
    private Long id;
    private String status;
    private Long eventoId;
    private Long recursoId;
    private String recursoNome;
}
