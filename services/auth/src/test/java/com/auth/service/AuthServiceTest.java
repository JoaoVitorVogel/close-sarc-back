package com.auth.service;

import com.auth.dto.LoginRequestDto;
import com.auth.dto.LoginResponseDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AuthService authService;

    private LoginRequestDto loginRequestDto;
    private Authentication authentication;
    private User user;
    private Admin admin;

    @BeforeEach
    void setUp() {
        loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        loginRequestDto.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("encoded");

        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@example.com");
        admin.setName("Admin User");
        admin.setPassword("encoded");
    }

    @Test
    void testLogin_WithUserRole_ShouldReturnLoginResponse() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_user");
        authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com",
                "password123",
                Collections.singletonList(authority)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("token123");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        LoginResponseDto response = authService.login(loginRequestDto);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("Test User", response.getName());
        assertEquals("ROLE_user", response.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(authentication);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testLogin_WithAdminRole_ShouldReturnLoginResponse() {
        loginRequestDto.setEmail("admin@example.com");
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authentication = new UsernamePasswordAuthenticationToken(
                "admin@example.com",
                "password123",
                Collections.singletonList(authority)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("token123");
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        LoginResponseDto response = authService.login(loginRequestDto);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("Admin User", response.getName());
        assertEquals("ROLE_ADMIN", response.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(authentication);
        verify(adminRepository).findByEmail("admin@example.com");
    }

    @Test
    void testLogin_WithUserNotFound_ShouldReturnEmptyName() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_user");
        authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com",
                "password123",
                Collections.singletonList(authority)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("token123");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        LoginResponseDto response = authService.login(loginRequestDto);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("", response.getName());
        assertEquals("ROLE_user", response.getRole());
    }

    @Test
    void testLogin_WithProfessorRole_ShouldReturnLoginResponse() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_PROFESSOR");
        authentication = new UsernamePasswordAuthenticationToken(
                "professor@example.com",
                "password123",
                Collections.singletonList(authority)
        );

        loginRequestDto.setEmail("professor@example.com");
        user.setEmail("professor@example.com");
        user.setName("Professor Test");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("token123");
        when(userRepository.findByEmail("professor@example.com")).thenReturn(Optional.of(user));

        LoginResponseDto response = authService.login(loginRequestDto);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("Professor Test", response.getName());
        assertEquals("ROLE_PROFESSOR", response.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(authentication);
        verify(userRepository).findByEmail("professor@example.com");
    }

    @Test
    void testLogin_WithUnknownRole_ShouldReturnEmptyName() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_UNKNOWN");
        authentication = new UsernamePasswordAuthenticationToken(
                "unknown@example.com",
                "password123",
                Collections.singletonList(authority)
        );

        loginRequestDto.setEmail("unknown@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("token123");

        LoginResponseDto response = authService.login(loginRequestDto);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("", response.getName());
        assertEquals("ROLE_UNKNOWN", response.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(authentication);
        verify(userRepository, never()).findByEmail(anyString());
        verify(adminRepository, never()).findByEmail(anyString());
    }

    @Test
    void testLogin_WithAdminNotFound_ShouldReturnEmptyName() {
        loginRequestDto.setEmail("admin@example.com");
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authentication = new UsernamePasswordAuthenticationToken(
                "admin@example.com",
                "password123",
                Collections.singletonList(authority)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("token123");
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        LoginResponseDto response = authService.login(loginRequestDto);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("", response.getName());
        assertEquals("ROLE_ADMIN", response.getRole());

        verify(adminRepository).findByEmail("admin@example.com");
    }
}

