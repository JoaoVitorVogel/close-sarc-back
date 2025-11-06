package com.user.repository;

import com.user.entity.Professor;
import com.user.entity.Turma;
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
class TurmaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TurmaRepository turmaRepository;

    private Professor professor;
    private Turma turma;

    @BeforeEach
    void setUp() {
        professor = new Professor();
        professor.setNome("Professor Teste");
        professor.setEmail("professor@example.com");
        professor.setSenha("encoded");
        entityManager.persistAndFlush(professor);

        turma = new Turma();
        turma.setNome("Turma A");
        turma.setCurso("Ciência da Computação");
        turma.setHorario("08:00-10:00");
        turma.setProfessor(professor);
    }

    @Test
    void testSave_ShouldPersistTurma() {
        Turma saved = turmaRepository.save(turma);

        assertNotNull(saved.getId());
        assertEquals("Turma A", saved.getNome());
        assertEquals("Ciência da Computação", saved.getCurso());
    }

    @Test
    void testFindByProfessor_Id_ShouldReturnTurmas() {
        entityManager.persistAndFlush(turma);

        Turma turma2 = new Turma();
        turma2.setNome("Turma B");
        turma2.setCurso("Engenharia de Software");
        turma2.setHorario("10:00-12:00");
        turma2.setProfessor(professor);
        entityManager.persistAndFlush(turma2);

        List<Turma> turmas = turmaRepository.findByProfessor_Id(professor.getId());

        assertEquals(2, turmas.size());
        assertEquals("Turma A", turmas.get(0).getNome());
    }

    @Test
    void testFindByProfessor_Id_WhenNoTurmas_ShouldReturnEmpty() {
        List<Turma> turmas = turmaRepository.findByProfessor_Id(999L);

        assertTrue(turmas.isEmpty());
    }
}

