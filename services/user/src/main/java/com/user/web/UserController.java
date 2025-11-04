package com.user.web;

import com.user.dto.*;
import com.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Operações para professores: recursos, reservas, eventos e turmas")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/recursos")
    @Operation(
            summary = "Listar recursos disponíveis",
            description = "Lista todos os recursos (salas, equipamentos, etc.) com opção de filtragem por disponibilidade"
    )
    public ResponseEntity<List<RecursoResponseDto>> listarRecursos(
            @Parameter(description = "Filtrar por disponibilidade (true/false). Se não informado, lista todos")
            @RequestParam(required = false) Boolean disponivel
    ) {
        List<RecursoResponseDto> recursos = userService.listarRecursos(disponivel);
        return ResponseEntity.ok(recursos);
    }

    @PostMapping("/reservas")
    @Operation(
            summary = "Criar reserva",
            description = "Cria uma nova reserva para um recurso. Valida se o recurso está disponível no horário solicitado."
    )
    public ResponseEntity<ReservaResponseDto> criarReserva(
            @Valid @RequestBody ReservaCreateDto dto
    ) {
        ReservaResponseDto reserva = userService.criarReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @PostMapping("/eventos")
    @Operation(
            summary = "Cadastrar evento",
            description = "Cadastra um novo evento vinculado a um recurso (ex: sala ou laboratório). " +
                         "Contém data, hora, descrição e responsável (professor). Cria automaticamente a reserva associada."
    )
    public ResponseEntity<EventoResponseDto> cadastrarEvento(
            @Valid @RequestBody EventoCreateDto dto
    ) {
        EventoResponseDto evento = userService.cadastrarEvento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(evento);
    }

    @PutMapping("/reservas/{id}/cancelar")
    @Operation(
            summary = "Cancelar reserva",
            description = "Cancela uma reserva existente, atualizando o status para 'CANCELADA'"
    )
    public ResponseEntity<ReservaResponseDto> cancelarReserva(
            @Parameter(description = "ID da reserva a ser cancelada")
            @PathVariable Long id
    ) {
        ReservaResponseDto reserva = userService.cancelarReserva(id);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/professores/{professorId}/turmas")
    @Operation(
            summary = "Listar turmas do professor",
            description = "Lista todas as turmas associadas ao professor logado"
    )
    public ResponseEntity<List<TurmaResponseDto>> listarTurmasProfessor(
            @Parameter(description = "ID do professor")
            @PathVariable Long professorId
    ) {
        List<TurmaResponseDto> turmas = userService.listarTurmasProfessor(professorId);
        return ResponseEntity.ok(turmas);
    }
}
