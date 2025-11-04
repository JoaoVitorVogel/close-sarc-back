package com.auth.repository;

import com.auth.entity.Admin;
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
class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setName("Admin User");
        admin.setPassword("encodedPassword");
    }

    @Test
    void testFindByEmail_WhenAdminExists_ShouldReturnAdmin() {
        entityManager.persistAndFlush(admin);

        Optional<Admin> found = adminRepository.findByEmail("admin@example.com");

        assertTrue(found.isPresent());
        assertEquals("admin@example.com", found.get().getEmail());
        assertEquals("Admin User", found.get().getName());
    }

    @Test
    void testFindByEmail_WhenAdminNotExists_ShouldReturnEmpty() {
        Optional<Admin> found = adminRepository.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent());
    }

    @Test
    void testSave_ShouldPersistAdmin() {
        Admin saved = adminRepository.save(admin);

        assertNotNull(saved.getId());
        assertEquals("admin@example.com", saved.getEmail());
    }

    @Test
    void testFindById_WhenAdminExists_ShouldReturnAdmin() {
        Admin persisted = entityManager.persistAndFlush(admin);

        Optional<Admin> found = adminRepository.findById(persisted.getId());

        assertTrue(found.isPresent());
        assertEquals(persisted.getId(), found.get().getId());
    }
}

