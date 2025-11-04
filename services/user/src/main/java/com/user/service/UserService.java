package com.user.service;

import com.user.dto.*;
import com.user.entity.*;
import com.user.exception.ResourceNotFoundException;
import com.user.exception.ResourceNotAvailableException;
import com.user.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    // 1. Listar recursos com filtro de disponibilidade
    public List<RecursoResponseDto> listarRecursos(Boolean disponivel) {
        List<Recurso> recursos;
        
        if (disponivel != null) {
            recursos = recursoRepository.findByDisponivel(disponivel);
        } else {
            recursos = recursoRepository.findAll();
        }
        
        return recursos.stream()
                .map(this::mapToRecursoResponseDto)
                .collect(Collectors.toList());
    }

    // 2. Criar reserva (validando disponibilidade)
    @Transactional
    public ReservaResponseDto criarReserva(ReservaCreateDto dto) {
        // Buscar evento
        Evento evento = eventoRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + dto.getEventoId()));

        // Buscar recurso
        Recurso recurso = recursoRepository.findById(dto.getRecursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com ID: " + dto.getRecursoId()));

        // Verificar se recurso está disponível
        if (!recurso.isDisponivel()) {
            throw new ResourceNotAvailableException("Recurso não está disponível para reserva");
        }

        // Verificar conflitos de horário
        List<Reserva> reservasConflitantes = reservaRepository.findReservasConflitantes(
                recurso.getId(),
                evento.getDataInicio(),
                evento.getDataFim()
        );

        if (!reservasConflitantes.isEmpty()) {
            throw new ResourceNotAvailableException(
                    "Recurso já está reservado no período solicitado"
            );
        }

        // Criar reserva
        Reserva reserva = new Reserva();
        reserva.setEvento(evento);
        reserva.setRecurso(recurso);
        reserva.setStatus("ATIVA");

        reserva = reservaRepository.save(reserva);

        return mapToReservaResponseDto(reserva);
    }

    // 3. Cadastrar evento (criando também a reserva)
    @Transactional
    public EventoResponseDto cadastrarEvento(EventoCreateDto dto) {
        // Validar data
        if (dto.getDataFim().isBefore(dto.getDataInicio()) || 
            dto.getDataFim().isEqual(dto.getDataInicio())) {
            throw new ResourceNotAvailableException("Data de fim deve ser posterior à data de início");
        }

        // Buscar professor
        Professor professor = professorRepository.findById(dto.getProfessorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + dto.getProfessorId()));

        // Criar evento
        Evento evento = new Evento();
        evento.setTitulo(dto.getTitulo());
        evento.setDescricao(dto.getDescricao());
        evento.setDataInicio(dto.getDataInicio());
        evento.setDataFim(dto.getDataFim());
        evento.setProfessor(professor);

        evento = eventoRepository.save(evento);

        // Buscar recurso
        Recurso recurso = recursoRepository.findById(dto.getRecursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com ID: " + dto.getRecursoId()));

        // Verificar disponibilidade e criar reserva
        if (!recurso.isDisponivel()) {
            throw new ResourceNotAvailableException("Recurso não está disponível para reserva");
        }

        // Verificar conflitos de horário
        List<Reserva> reservasConflitantes = reservaRepository.findReservasConflitantes(
                recurso.getId(),
                evento.getDataInicio(),
                evento.getDataFim()
        );

        if (!reservasConflitantes.isEmpty()) {
            throw new ResourceNotAvailableException(
                    "Recurso já está reservado no período solicitado"
            );
        }

        // Criar reserva associada ao evento
        Reserva reserva = new Reserva();
        reserva.setEvento(evento);
        reserva.setRecurso(recurso);
        reserva.setStatus("ATIVA");
        reservaRepository.save(reserva);

        return mapToEventoResponseDto(evento);
    }

    // 4. Cancelar reserva
    @Transactional
    public ReservaResponseDto cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + reservaId));

        if ("CANCELADA".equals(reserva.getStatus())) {
            throw new ResourceNotAvailableException("Reserva já está cancelada");
        }

        reserva.setStatus("CANCELADA");
        reserva = reservaRepository.save(reserva);

        return mapToReservaResponseDto(reserva);
    }

    // 5. Listar turmas do professor
    public List<TurmaResponseDto> listarTurmasProfessor(Long professorId) {
        // Verificar se professor existe
        professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + professorId));

        List<Turma> turmas = turmaRepository.findByProfessor_Id(professorId);

        return turmas.stream()
                .map(this::mapToTurmaResponseDto)
                .collect(Collectors.toList());
    }

    // Mappers auxiliares
    private RecursoResponseDto mapToRecursoResponseDto(Recurso recurso) {
        RecursoResponseDto dto = new RecursoResponseDto();
        dto.setId(recurso.getId());
        dto.setNome(recurso.getNome());
        dto.setDescricao(recurso.getDescricao());
        dto.setDisponivel(recurso.isDisponivel());
        return dto;
    }

    private ReservaResponseDto mapToReservaResponseDto(Reserva reserva) {
        ReservaResponseDto dto = new ReservaResponseDto();
        dto.setId(reserva.getId());
        dto.setStatus(reserva.getStatus());
        dto.setEventoId(reserva.getEvento().getId());
        dto.setRecursoId(reserva.getRecurso().getId());
        dto.setRecursoNome(reserva.getRecurso().getNome());
        return dto;
    }

    private EventoResponseDto mapToEventoResponseDto(Evento evento) {
        EventoResponseDto dto = new EventoResponseDto();
        dto.setId(evento.getId());
        dto.setTitulo(evento.getTitulo());
        dto.setDescricao(evento.getDescricao());
        dto.setDataInicio(evento.getDataInicio());
        dto.setDataFim(evento.getDataFim());
        dto.setProfessorId(evento.getProfessor().getId());
        dto.setProfessorNome(evento.getProfessor().getNome());
        return dto;
    }

    private TurmaResponseDto mapToTurmaResponseDto(Turma turma) {
        TurmaResponseDto dto = new TurmaResponseDto();
        dto.setId(turma.getId());
        dto.setNome(turma.getNome());
        dto.setCurso(turma.getCurso());
        dto.setHorario(turma.getHorario());
        dto.setProfessorId(turma.getProfessor().getId());
        dto.setProfessorNome(turma.getProfessor().getNome());
        return dto;
    }
}
