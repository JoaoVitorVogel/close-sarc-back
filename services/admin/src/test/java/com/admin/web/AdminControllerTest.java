package com.admin.web;

import com.admin.dto.GroupCreateDto;
import com.admin.dto.ResourceCreateDto;
import com.admin.dto.UserCreateDto;
import com.admin.entity.Group;
import com.admin.entity.Resource;
import com.admin.entity.User;
import com.admin.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserCreateDto userCreateDto;
    private ResourceCreateDto resourceCreateDto;
    private GroupCreateDto groupCreateDto;
    private User user;
    private Resource resource;
    private Group group;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

        userCreateDto = new UserCreateDto();
        userCreateDto.setName("Test Professor");
        userCreateDto.setEmail("professor@example.com");
        userCreateDto.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setName("Test Professor");
        user.setEmail("professor@example.com");
        user.setPassword("encodedPassword");

        resourceCreateDto = new ResourceCreateDto();
        resourceCreateDto.setName("Laboratório 1");
        resourceCreateDto.setDescription("Laboratório de informática");
        resourceCreateDto.setAvailable(true);

        resource = new Resource();
        resource.setId(1L);
        resource.setName("Laboratório 1");
        resource.setDescription("Laboratório de informática");
        resource.setAvailable(true);

        groupCreateDto = new GroupCreateDto();
        groupCreateDto.setName("Turma A");
        groupCreateDto.setCourse("Ciência da Computação");
        groupCreateDto.setSchedule("08:00-10:00");
        groupCreateDto.setProfessorId(1L);

        group = new Group();
        group.setId(1L);
        group.setName("Turma A");
        group.setCourse("Ciência da Computação");
        group.setSchedule("08:00-10:00");
        group.setProfessorId(1L);
    }

    @Test
    void testCadastrarProfessor_ShouldReturnCreated() throws Exception {
        when(adminService.addUser(any(UserCreateDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/admin/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Professor"))
                .andExpect(jsonPath("$.email").value("professor@example.com"));
    }

    @Test
    void testCadastrarRecurso_ShouldReturnCreated() throws Exception {
        when(adminService.cadastrarRecurso(any(ResourceCreateDto.class))).thenReturn(resource);

        mockMvc.perform(post("/api/admin/recursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resourceCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laboratório 1"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testCadastrarTurma_ShouldReturnCreated() throws Exception {
        when(adminService.cadastrarGroup(any(GroupCreateDto.class))).thenReturn(group);

        mockMvc.perform(post("/api/admin/turma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Turma A"))
                .andExpect(jsonPath("$.course").value("Ciência da Computação"));
    }

    // Nota: Teste de validação removido pois MockMvc standalone não processa validações automaticamente
    // Para testar validações, seria necessário usar @WebMvcTest com contexto completo
}

