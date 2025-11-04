package com.admin.repository;

import com.admin.entity.Group;
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
class GroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    private Group group;

    @BeforeEach
    void setUp() {
        group = new Group();
        group.setName("Turma A");
        group.setCourse("Ciência da Computação");
        group.setSchedule("08:00-10:00");
        group.setProfessorId(1L);
    }

    @Test
    void testSave_ShouldPersistGroup() {
        Group saved = groupRepository.save(group);

        assertNotNull(saved.getId());
        assertEquals("Turma A", saved.getName());
        assertEquals("Ciência da Computação", saved.getCourse());
        assertEquals(1L, saved.getProfessorId());
    }

    @Test
    void testFindById_WhenGroupExists_ShouldReturnGroup() {
        Group persisted = entityManager.persistAndFlush(group);

        Optional<Group> found = groupRepository.findById(persisted.getId());

        assertTrue(found.isPresent());
        assertEquals(persisted.getId(), found.get().getId());
        assertEquals("Turma A", found.get().getName());
    }

    @Test
    void testFindById_WhenGroupNotExists_ShouldReturnEmpty() {
        Optional<Group> found = groupRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnAllGroups() {
        entityManager.persistAndFlush(group);

        Group group2 = new Group();
        group2.setName("Turma B");
        group2.setCourse("Engenharia de Software");
        group2.setSchedule("10:00-12:00");
        group2.setProfessorId(2L);
        entityManager.persistAndFlush(group2);

        assertEquals(2, groupRepository.findAll().size());
    }
}

