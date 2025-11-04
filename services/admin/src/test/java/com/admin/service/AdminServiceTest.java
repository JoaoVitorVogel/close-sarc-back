package com.admin.service;

import com.admin.dto.GroupCreateDto;
import com.admin.dto.ResourceCreateDto;
import com.admin.dto.UserCreateDto;
import com.admin.entity.Group;
import com.admin.entity.Resource;
import com.admin.entity.User;
import com.admin.repository.GroupRepository;
import com.admin.repository.ResourceRepository;
import com.admin.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private UserCreateDto userCreateDto;
    private ResourceCreateDto resourceCreateDto;
    private GroupCreateDto groupCreateDto;
    private User savedUser;
    private Resource savedResource;
    private Group savedGroup;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto();
        userCreateDto.setName("Test Professor");
        userCreateDto.setEmail("professor@example.com");
        userCreateDto.setPassword("password123");

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Test Professor");
        savedUser.setEmail("professor@example.com");
        savedUser.setPassword("encodedPassword");

        resourceCreateDto = new ResourceCreateDto();
        resourceCreateDto.setName("Laboratório 1");
        resourceCreateDto.setDescription("Laboratório de informática");
        resourceCreateDto.setAvailable(true);

        savedResource = new Resource();
        savedResource.setId(1L);
        savedResource.setName("Laboratório 1");
        savedResource.setDescription("Laboratório de informática");
        savedResource.setAvailable(true);

        groupCreateDto = new GroupCreateDto();
        groupCreateDto.setName("Turma A");
        groupCreateDto.setCourse("Ciência da Computação");
        groupCreateDto.setSchedule("08:00-10:00");
        groupCreateDto.setProfessorId(1L);

        savedGroup = new Group();
        savedGroup.setId(1L);
        savedGroup.setName("Turma A");
        savedGroup.setCourse("Ciência da Computação");
        savedGroup.setSchedule("08:00-10:00");
        savedGroup.setProfessorId(1L);
    }

    @Test
    void testAddUser_ShouldEncryptPasswordAndSave() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = adminService.addUser(userCreateDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Professor", result.getName());
        assertEquals("professor@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCadastrarRecurso_WithAvailable_ShouldSaveWithAvailable() {
        when(resourceRepository.save(any(Resource.class))).thenReturn(savedResource);

        Resource result = adminService.cadastrarRecurso(resourceCreateDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laboratório 1", result.getName());
        assertTrue(result.isAvailable());

        verify(resourceRepository).save(any(Resource.class));
    }

    @Test
    void testCadastrarRecurso_WithoutAvailable_ShouldDefaultToTrue() {
        resourceCreateDto.setAvailable(null);
        savedResource.setAvailable(true);

        when(resourceRepository.save(any(Resource.class))).thenReturn(savedResource);

        Resource result = adminService.cadastrarRecurso(resourceCreateDto);

        assertNotNull(result);
        assertTrue(result.isAvailable());
        verify(resourceRepository).save(any(Resource.class));
    }

    @Test
    void testCadastrarGroup_ShouldSaveGroup() {
        when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);

        Group result = adminService.cadastrarGroup(groupCreateDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Turma A", result.getName());
        assertEquals("Ciência da Computação", result.getCourse());
        assertEquals(1L, result.getProfessorId());

        verify(groupRepository).save(any(Group.class));
    }
}

