package com.admin.web;

import com.admin.dto.GroupCreateDto;
import com.admin.dto.ResourceCreateDto;
import com.admin.dto.UserCreateDto;
import com.admin.entity.Group;
import com.admin.entity.Resource;
import com.admin.entity.User;
import com.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; 
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Operações de administração: professores, recursos e turmas")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/professores")
    @Operation(summary = "Cadastrar professor", description = "Cria um novo professor a partir do DTO informado.")
    public ResponseEntity<User> cadastrarProfessor(@Valid @RequestBody UserCreateDto dto) {
        User newUser = adminService.addUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/recursos")
    @Operation(summary = "Cadastrar recurso", description = "Cria um novo recurso (Resource) a partir do DTO informado.")
    public ResponseEntity<Resource> cadastrarRecurso(@Valid @RequestBody ResourceCreateDto dto) {
        Resource novoRecurso = adminService.cadastrarRecurso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRecurso);
    }

    @PostMapping("/turma")
    @Operation(summary = "Cadastrar turma", description = "Cria uma nova turma (Group) a partir do DTO informado.")
    public ResponseEntity<Group> cadastrarGroup(@Valid @RequestBody GroupCreateDto dto) {
        Group novaTurma = adminService.cadastrarGroup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTurma);
    }
}
