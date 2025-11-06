package com.user.repository;

import com.user.entity.Recurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

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
class RecursoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecursoRepository recursoRepository;

    private Recurso recurso;

    @BeforeEach
    void setUp() {
        recurso = new Recurso();
        recurso.setNome("Laboratório 1");
        recurso.setDescricao("Laboratório de informática");
        recurso.setDisponivel(true);
    }

    @Test
    void testSave_ShouldPersistRecurso() {
        Recurso saved = recursoRepository.save(recurso);

        assertNotNull(saved.getId());
        assertEquals("Laboratório 1", saved.getNome());
        assertTrue(saved.isDisponivel());
    }

    @Test
    void testFindByDisponivel_ShouldReturnFiltered() {
        entityManager.persistAndFlush(recurso);

        Recurso recurso2 = new Recurso();
        recurso2.setNome("Laboratório 2");
        recurso2.setDescricao("Laboratório de química");
        recurso2.setDisponivel(false);
        entityManager.persistAndFlush(recurso2);

        List<Recurso> disponiveis = recursoRepository.findByDisponivel(true);
        assertEquals(1, disponiveis.size());
        assertTrue(disponiveis.get(0).isDisponivel());

        List<Recurso> indisponiveis = recursoRepository.findByDisponivel(false);
        assertEquals(1, indisponiveis.size());
        assertFalse(indisponiveis.get(0).isDisponivel());
    }

    @Test
    void testFindAll_ShouldReturnAllRecursos() {
        entityManager.persistAndFlush(recurso);

        Recurso recurso2 = new Recurso();
        recurso2.setNome("Laboratório 2");
        recurso2.setDescricao("Laboratório de química");
        recurso2.setDisponivel(false);
        entityManager.persistAndFlush(recurso2);

        assertEquals(2, recursoRepository.findAll().size());
    }
}

