package com.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaCreateDto {

    @NotNull(message = "O ID do evento é obrigatório")
    private Long eventoId;

    @NotNull(message = "O ID do recurso é obrigatório")
    private Long recursoId;

    // Getters explícitos para garantir compatibilidade
    public Long getEventoId() {
        return eventoId;
    }

    public Long getRecursoId() {
        return recursoId;
    }
}
