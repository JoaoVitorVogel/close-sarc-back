package com.user.service;

import com.user.dto.*;
import com.user.entity.*;
import com.user.exception.ResourceNotFoundException;
import com.user.exception.ResourceNotAvailableException;
import com.user.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private RecursoRepository recursoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private TurmaRepository turmaRepository;

    @InjectMocks
    private UserService userService;

    private Recurso recurso;
    private Evento evento;
    private Professor professor;
    private Turma turma;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        recurso = new Recurso();
        recurso.setId(1L);
        recurso.setNome("Laboratório 1");
        recurso.setDescricao("Laboratório de informática");
        recurso.setDisponivel(true);

        professor = new Professor();
        professor.setId(1L);
        professor.setNome("Professor Teste");
        professor.setEmail("professor@example.com");

        evento = new Evento();
        evento.setId(1L);
        evento.setTitulo("Aula de Matemática");
        evento.setDescricao("Aula sobre álgebra");
        evento.setDataInicio(LocalDateTime.now());
        evento.setDataFim(LocalDateTime.now().plusHours(2));
        evento.setProfessor(professor);

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatus("ATIVA");
        reserva.setEvento(evento);
        reserva.setRecurso(recurso);

        turma = new Turma();
        turma.setId(1L);
        turma.setNome("Turma A");
        turma.setCurso("Ciência da Computação");
        turma.setHorario("08:00-10:00");
        turma.setProfessor(professor);
    }

    @Test
    void testListarRecursos_WithoutFilter_ShouldReturnAll() {
        List<Recurso> recursos = Arrays.asList(recurso);
        when(recursoRepository.findAll()).thenReturn(recursos);

        List<RecursoResponseDto> result = userService.listarRecursos(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laboratório 1", result.get(0).getNome());
        verify(recursoRepository).findAll();
    }

    @Test
    void testListarRecursos_WithFilter_ShouldReturnFiltered() {
        List<Recurso> recursos = Arrays.asList(recurso);
        when(recursoRepository.findByDisponivel(true)).thenReturn(recursos);

        List<RecursoResponseDto> result = userService.listarRecursos(true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isDisponivel());
        verify(recursoRepository).findByDisponivel(true);
    }

    @Test
    void testCriarReserva_ShouldCreateReserva() {
        ReservaCreateDto dto = new ReservaCreateDto();
        dto.setEventoId(1L);
        dto.setRecursoId(1L);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(reservaRepository.findReservasConflitantes(anyLong(), any(), any())).thenReturn(new ArrayList<>());
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        ReservaResponseDto result = userService.criarReserva(dto);

        assertNotNull(result);
        assertEquals("ATIVA", result.getStatus());
        verify(eventoRepository).findById(1L);
        verify(recursoRepository).findById(1L);
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void testCriarReserva_WhenEventoNotFound_ShouldThrowException() {
        ReservaCreateDto dto = new ReservaCreateDto();
        dto.setEventoId(999L);
        dto.setRecursoId(1L);

        when(eventoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.criarReserva(dto));
    }

    @Test
    void testCriarReserva_WhenRecursoNotAvailable_ShouldThrowException() {
        ReservaCreateDto dto = new ReservaCreateDto();
        dto.setEventoId(1L);
        dto.setRecursoId(1L);

        recurso.setDisponivel(false);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));

        assertThrows(ResourceNotAvailableException.class, () -> userService.criarReserva(dto));
    }

    @Test
    void testCriarReserva_WhenConflictExists_ShouldThrowException() {
        ReservaCreateDto dto = new ReservaCreateDto();
        dto.setEventoId(1L);
        dto.setRecursoId(1L);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(reservaRepository.findReservasConflitantes(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(reserva));

        assertThrows(ResourceNotAvailableException.class, () -> userService.criarReserva(dto));
    }

    @Test
    void testCadastrarEvento_ShouldCreateEventoAndReserva() {
        EventoCreateDto dto = new EventoCreateDto();
        dto.setTitulo("Aula de Matemática");
        dto.setDescricao("Aula sobre álgebra");
        dto.setDataInicio(LocalDateTime.now());
        dto.setDataFim(LocalDateTime.now().plusHours(2));
        dto.setProfessorId(1L);
        dto.setRecursoId(1L);

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(reservaRepository.findReservasConflitantes(anyLong(), any(), any())).thenReturn(new ArrayList<>());
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        EventoResponseDto result = userService.cadastrarEvento(dto);

        assertNotNull(result);
        assertEquals("Aula de Matemática", result.getTitulo());
        verify(professorRepository).findById(1L);
        verify(eventoRepository).save(any(Evento.class));
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void testCadastrarEvento_WithInvalidDates_ShouldThrowException() {
        LocalDateTime mesmaData = LocalDateTime.now();
        EventoCreateDto dto = new EventoCreateDto();
        dto.setTitulo("Aula de Matemática");
        dto.setDataInicio(mesmaData);
        dto.setDataFim(mesmaData); // Mesma data = inválido
        dto.setProfessorId(1L);
        dto.setRecursoId(1L);

        // A validação de datas acontece ANTES de buscar recursos/professores
        assertThrows(ResourceNotAvailableException.class, () -> userService.cadastrarEvento(dto));
    }

    @Test
    void testCancelarReserva_ShouldCancelReserva() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        ReservaResponseDto result = userService.cancelarReserva(1L);

        assertNotNull(result);
        assertEquals("CANCELADA", result.getStatus());
        verify(reservaRepository).findById(1L);
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void testCancelarReserva_WhenAlreadyCancelled_ShouldThrowException() {
        reserva.setStatus("CANCELADA");
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        assertThrows(ResourceNotAvailableException.class, () -> userService.cancelarReserva(1L));
    }

    @Test
    void testListarTurmasProfessor_ShouldReturnTurmas() {
        List<Turma> turmas = Arrays.asList(turma);
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(turmaRepository.findByProfessor_Id(1L)).thenReturn(turmas);

        List<TurmaResponseDto> result = userService.listarTurmasProfessor(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Turma A", result.get(0).getNome());
        verify(professorRepository).findById(1L);
        verify(turmaRepository).findByProfessor_Id(1L);
    }

    @Test
    void testListarTurmasProfessor_WhenProfessorNotFound_ShouldThrowException() {
        when(professorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.listarTurmasProfessor(999L));
    }
}

