package com.admin.repository;

import com.admin.entity.User;
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
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("professor@example.com");
        user.setName("Test Professor");
        user.setPassword("encodedPassword");
    }

    @Test
    void testSave_ShouldPersistUser() {
        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("professor@example.com", saved.getEmail());
        assertEquals("Test Professor", saved.getName());
    }

    @Test
    void testFindById_WhenUserExists_ShouldReturnUser() {
        User persisted = entityManager.persistAndFlush(user);

        Optional<User> found = userRepository.findById(persisted.getId());

        assertTrue(found.isPresent());
        assertEquals(persisted.getId(), found.get().getId());
        assertEquals("professor@example.com", found.get().getEmail());
    }

    @Test
    void testFindById_WhenUserNotExists_ShouldReturnEmpty() {
        Optional<User> found = userRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnAllUsers() {
        entityManager.persistAndFlush(user);

        User user2 = new User();
        user2.setEmail("professor2@example.com");
        user2.setName("Test Professor 2");
        user2.setPassword("encodedPassword2");
        entityManager.persistAndFlush(user2);

        assertEquals(2, userRepository.findAll().size());
    }

    @Test
    void testDelete_ShouldRemoveUser() {
        User persisted = entityManager.persistAndFlush(user);

        userRepository.deleteById(persisted.getId());
        entityManager.flush();

        assertFalse(userRepository.findById(persisted.getId()).isPresent());
    }
}

