package com.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO para criação de evento")
public class EventoCreateDto {

    @NotBlank(message = "O título do evento é obrigatório")
    @Schema(description = "Título do evento", example = "Aula de Matemática", required = true)
    private String titulo;

    @Schema(description = "Descrição do evento", example = "Aula sobre álgebra linear")
    private String descricao;

    @NotNull(message = "A data de início é obrigatória")
    @Schema(description = "Data e hora de início do evento", example = "2025-11-04T08:00:00", required = true)
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim é obrigatória")
    @Schema(description = "Data e hora de fim do evento", example = "2025-11-04T10:00:00", required = true)
    private LocalDateTime dataFim;

    @NotNull(message = "O ID do professor é obrigatório")
    @Schema(description = "ID do professor responsável", example = "1", required = true)
    private Long professorId;

    @NotNull(message = "O ID do recurso é obrigatório")
    @Schema(description = "ID do recurso (sala, laboratório, etc.)", example = "1", required = true)
    private Long recursoId;
}
