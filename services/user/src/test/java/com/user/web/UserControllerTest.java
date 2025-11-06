package com.user.web;

import com.user.dto.*;
import com.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private RecursoResponseDto recursoResponseDto;
    private ReservaResponseDto reservaResponseDto;
    private EventoResponseDto eventoResponseDto;
    private TurmaResponseDto turmaResponseDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        recursoResponseDto = new RecursoResponseDto();
        recursoResponseDto.setId(1L);
        recursoResponseDto.setNome("Laboratório 1");
        recursoResponseDto.setDescricao("Laboratório de informática");
        recursoResponseDto.setDisponivel(true);

        reservaResponseDto = new ReservaResponseDto();
        reservaResponseDto.setId(1L);
        reservaResponseDto.setStatus("ATIVA");
        reservaResponseDto.setEventoId(1L);
        reservaResponseDto.setRecursoId(1L);
        reservaResponseDto.setRecursoNome("Laboratório 1");

        eventoResponseDto = new EventoResponseDto();
        eventoResponseDto.setId(1L);
        eventoResponseDto.setTitulo("Aula de Matemática");
        eventoResponseDto.setDescricao("Aula sobre álgebra");
        eventoResponseDto.setDataInicio(LocalDateTime.now());
        eventoResponseDto.setDataFim(LocalDateTime.now().plusHours(2));
        eventoResponseDto.setProfessorId(1L);
        eventoResponseDto.setProfessorNome("Professor Teste");

        turmaResponseDto = new TurmaResponseDto();
        turmaResponseDto.setId(1L);
        turmaResponseDto.setNome("Turma A");
        turmaResponseDto.setCurso("Ciência da Computação");
        turmaResponseDto.setHorario("08:00-10:00");
        turmaResponseDto.setProfessorId(1L);
        turmaResponseDto.setProfessorNome("Professor Teste");
    }

    @Test
    void testListarRecursos_ShouldReturnOk() throws Exception {
        List<RecursoResponseDto> recursos = Arrays.asList(recursoResponseDto);
        when(userService.listarRecursos(null)).thenReturn(recursos);

        mockMvc.perform(get("/api/user/recursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Laboratório 1"));
    }

    @Test
    void testListarRecursos_WithFilter_ShouldReturnFiltered() throws Exception {
        List<RecursoResponseDto> recursos = Arrays.asList(recursoResponseDto);
        when(userService.listarRecursos(true)).thenReturn(recursos);

        mockMvc.perform(get("/api/user/recursos").param("disponivel", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].disponivel").value(true));
    }

    @Test
    void testCriarReserva_ShouldReturnCreated() throws Exception {
        ReservaCreateDto reservaCreateDto = new ReservaCreateDto();
        reservaCreateDto.setEventoId(1L);
        reservaCreateDto.setRecursoId(1L);

        when(userService.criarReserva(any(ReservaCreateDto.class))).thenReturn(reservaResponseDto);

        mockMvc.perform(post("/api/user/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ATIVA"));
    }

    @Test
    void testCadastrarEvento_ShouldReturnCreated() throws Exception {
        EventoCreateDto eventoCreateDto = new EventoCreateDto();
        eventoCreateDto.setTitulo("Aula de Matemática");
        eventoCreateDto.setDescricao("Aula sobre álgebra");
        eventoCreateDto.setDataInicio(LocalDateTime.now());
        eventoCreateDto.setDataFim(LocalDateTime.now().plusHours(2));
        eventoCreateDto.setProfessorId(1L);
        eventoCreateDto.setRecursoId(1L);

        when(userService.cadastrarEvento(any(EventoCreateDto.class))).thenReturn(eventoResponseDto);

        mockMvc.perform(post("/api/user/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventoCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Aula de Matemática"));
    }

    @Test
    void testCancelarReserva_ShouldReturnOk() throws Exception {
        reservaResponseDto.setStatus("CANCELADA");
        when(userService.cancelarReserva(1L)).thenReturn(reservaResponseDto);

        mockMvc.perform(put("/api/user/reservas/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
    }

    @Test
    void testListarTurmasProfessor_ShouldReturnOk() throws Exception {
        List<TurmaResponseDto> turmas = Arrays.asList(turmaResponseDto);
        when(userService.listarTurmasProfessor(1L)).thenReturn(turmas);

        mockMvc.perform(get("/api/user/professores/1/turmas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Turma A"));
    }
}

