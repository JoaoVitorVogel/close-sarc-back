package com.user.repository;

import com.user.entity.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientWebSecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class
        }
)
@ActiveProfiles("test")
class ProfessorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProfessorRepository professorRepository;

    private Professor professor;

    @BeforeEach
    void setUp() {
        professor = new Professor();
        professor.setNome("Professor Teste");
        professor.setEmail("professor@example.com");
        professor.setSenha("encodedPassword");
    }

    @Test
    void testSave_ShouldPersistProfessor() {
        Professor saved = professorRepository.save(professor);

        assertNotNull(saved.getId());
        assertEquals("professor@example.com", saved.getEmail());
        assertEquals("Professor Teste", saved.getNome());
    }

    @Test
    void testFindByEmail_WhenProfessorExists_ShouldReturnProfessor() {
        entityManager.persistAndFlush(professor);

        Optional<Professor> found = professorRepository.findByEmail("professor@example.com");

        assertTrue(found.isPresent());
        assertEquals("professor@example.com", found.get().getEmail());
    }

    @Test
    void testFindByEmail_WhenProfessorNotExists_ShouldReturnEmpty() {
        Optional<Professor> found = professorRepository.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindById_WhenProfessorExists_ShouldReturnProfessor() {
        Professor persisted = entityManager.persistAndFlush(professor);

        Optional<Professor> found = professorRepository.findById(persisted.getId());

        assertTrue(found.isPresent());
        assertEquals(persisted.getId(), found.get().getId());
    }
}

