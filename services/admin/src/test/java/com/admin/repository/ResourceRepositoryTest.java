package com.admin.repository;

import com.admin.entity.Resource;
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
class ResourceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResourceRepository resourceRepository;

    private Resource resource;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        resource.setName("Laboratório 1");
        resource.setDescription("Laboratório de informática");
        resource.setAvailable(true);
    }

    @Test
    void testSave_ShouldPersistResource() {
        Resource saved = resourceRepository.save(resource);

        assertNotNull(saved.getId());
        assertEquals("Laboratório 1", saved.getName());
        assertTrue(saved.isAvailable());
    }

    @Test
    void testFindById_WhenResourceExists_ShouldReturnResource() {
        Resource persisted = entityManager.persistAndFlush(resource);

        Optional<Resource> found = resourceRepository.findById(persisted.getId());

        assertTrue(found.isPresent());
        assertEquals(persisted.getId(), found.get().getId());
        assertEquals("Laboratório 1", found.get().getName());
    }

    @Test
    void testFindById_WhenResourceNotExists_ShouldReturnEmpty() {
        Optional<Resource> found = resourceRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnAllResources() {
        entityManager.persistAndFlush(resource);

        Resource resource2 = new Resource();
        resource2.setName("Laboratório 2");
        resource2.setDescription("Laboratório de química");
        resource2.setAvailable(false);
        entityManager.persistAndFlush(resource2);

        assertEquals(2, resourceRepository.findAll().size());
    }
}

