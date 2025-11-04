package com.user.repository;

import com.user.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientWebSecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class
        }
)
@ActiveProfiles("test")
class ReservaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservaRepository reservaRepository;

    private Professor professor;
    private Recurso recurso;
    private Evento evento;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        professor = new Professor();
        professor.setNome("Professor Teste");
        professor.setEmail("professor@example.com");
        professor.setSenha("encoded");
        entityManager.persistAndFlush(professor);

        recurso = new Recurso();
        recurso.setNome("Laboratório 1");
        recurso.setDescricao("Laboratório de informática");
        recurso.setDisponivel(true);
        entityManager.persistAndFlush(recurso);

        evento = new Evento();
        evento.setTitulo("Aula de Matemática");
        evento.setDescricao("Aula sobre álgebra");
        evento.setDataInicio(LocalDateTime.now());
        evento.setDataFim(LocalDateTime.now().plusHours(2));
        evento.setProfessor(professor);
        entityManager.persistAndFlush(evento);

        reserva = new Reserva();
        reserva.setStatus("ATIVA");
        reserva.setEvento(evento);
        reserva.setRecurso(recurso);
    }

    @Test
    void testSave_ShouldPersistReserva() {
        Reserva saved = reservaRepository.save(reserva);

        assertNotNull(saved.getId());
        assertEquals("ATIVA", saved.getStatus());
        assertNotNull(saved.getEvento());
        assertNotNull(saved.getRecurso());
    }

    @Test
    void testFindReservasConflitantes_ShouldFindConflicts() {
        entityManager.persistAndFlush(reserva);

        LocalDateTime inicio = LocalDateTime.now().minusHours(1);
        LocalDateTime fim = LocalDateTime.now().plusHours(1);

        List<Reserva> conflitantes = reservaRepository.findReservasConflitantes(
                recurso.getId(), inicio, fim);

        assertFalse(conflitantes.isEmpty());
        assertEquals(1, conflitantes.size());
    }

    @Test
    void testFindReservasConflitantes_WhenNoConflict_ShouldReturnEmpty() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = LocalDateTime.now().plusDays(1).plusHours(2);

        List<Reserva> conflitantes = reservaRepository.findReservasConflitantes(
                recurso.getId(), inicio, fim);

        assertTrue(conflitantes.isEmpty());
    }
}

