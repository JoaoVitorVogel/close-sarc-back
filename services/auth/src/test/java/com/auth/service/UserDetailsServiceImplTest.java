package com.auth.service;

import com.auth.entity.Admin;
import com.auth.entity.User;
import com.auth.repository.AdminRepository;
import com.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;
    private Admin admin;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("hashedPassword");
        user.setName("Test User");

        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@example.com");
        admin.setPassword("hashedPassword");
        admin.setName("Test Admin");
    }

    @Test
    void testLoadUserByUsername_WithUser_ShouldReturnUserDetails() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("user@example.com");

        assertNotNull(userDetails);
        assertEquals("user@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_user")));

        verify(userRepository).findByEmail("user@example.com");
        verify(adminRepository, never()).findByEmail(anyString());
    }

    @Test
    void testLoadUserByUsername_WithAdmin_ShouldReturnUserDetails() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");

        assertNotNull(userDetails);
        assertEquals("admin@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository).findByEmail("admin@example.com");
        verify(adminRepository).findByEmail("admin@example.com");
    }

    @Test
    void testLoadUserByUsername_WithNotFound_ShouldThrowException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("notfound@example.com");
        });

        verify(userRepository).findByEmail("notfound@example.com");
        verify(adminRepository).findByEmail("notfound@example.com");
    }
}

